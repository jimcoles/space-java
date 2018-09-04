/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.*;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Utility methods for querying and traversing the Space AST, which can also be thought of as a directory of nodes.
 *
 * @author Jim Coles
 */
public class AstUtils {

    private static final Logger log = Logger.getLogger(AstUtils.class);

    /** The nearest named parent of an element, e, is the the element thru which 'e' may be referenced. */
    public static NamedElement getNearestNamedParent(ModelElement anElem) {
        NamedElement lexParent = null;
        if (anElem.getParent() instanceof NamedElement
            && ((NamedElement) anElem.getParent()).isNamed())
        {
            lexParent = (NamedElement) anElem.getParent();
        }
        else if (anElem.getParent() != null) {
            lexParent = getNearestNamedParent(anElem.getParent());
        }
        return lexParent;
    }

    /** The lexical parent of an element, e, is the the element by which 'e' may be referenced. */
    public static ModelElement getNearestScopeParent(ModelElement anElem) {
        ModelElement lexParent = null;
        if (hasAnnotation(anElem.getParent(), LexicalNode.class)) {
            lexParent = anElem.getParent();
        }
        else if (anElem.getParent() != null) {
            lexParent = getNearestScopeParent(anElem.getParent());
        }
        return lexParent;
    }

    public static Directory getRunDir(Directory thisDirectory) {
        Directory directory =
            (Directory) CollectionUtils.find(thisDirectory.getChildren(),
                                          object -> object instanceof Directory);
        return directory;
    }

    public static boolean isGroupingNode(ModelElement child) {
        return child.isGroupingNode() || hasAnnotation(child, GroupingNode.class);
    }

    private static boolean hasAnnotation(ModelElement child, Class annotationClass) {
        return child.getClass().getAnnotation(annotationClass) != null;
    }

    /**
     * Entry point for resolving static references by the linker prior to execution.
     * Rules for resolution vary depending on the type of object referenced:
     *
     * Ref -> Type: must be fully qualified, possibly via an 'import'.
     * - Linker: Simple locate from root of AST.
     * - Exe: (no need)
     *
     * Ref -> Datum:
     *  - Linker: If first node in path (including a 1-length path), resolve
     *  according to this search order (precedence):
     *
     *  1. block local -> parent blocks(s)
     *  2. function args (if ref is inside function body)
     *  3. object member datums
     *  4. static/shared datums, fully qualified
     *
     *    If not first in chain, resolve with respect to type of preceding object.
     *
     *  - Exe: resolves to value or ref.
     *
     * Ref -> Function:
     *  1. "context object" member functions
     *  2. static functions, fully qualified
     *
     * @param dirChain
     * @param reference
     * @return
     */
    public static NamedElement resolveAstPath(List<Directory> dirChain, MetaReference reference) {
        NamedElement lookup = null;
        switch (reference.getTargetMetaType()) {
            case TYPE:
                lookup = checkIntrinsics(reference.getFirstPart().getNamePartExpr());
                if (lookup == null && reference.hasParent()) {
                    lookup = resolveFromNode(reference.getNamedParent(), reference);
                }
                if (lookup == null) {
                    lookup = resolveFromRoot(dirChain, reference);
                }
                break;
            case DATUM:
                lookup = findInNearestScope(AstUtils.getNearestScopeParent(reference), reference, null);
                if (lookup == null) {
                    lookup = resolveFromRoot(dirChain, reference);
                    if (lookup != null)
                        reference.setResolvedDatumScope(ScopeKind.STATIC);
                }
                break;
            case FUNCTION:
                lookup = resolveFromRoot(dirChain, reference);
                if (lookup == null && !reference.getFirstPart().hasNextExpr())
                    lookup = findInNearestScope(AstUtils.getNearestScopeParent(reference), reference, null);
                else if (lookup == null) {
                    lookup = resolveFromNode(reference.getNamedParent(), reference);
                }
                break;
        }
        if (reference instanceof TypeRefImpl && ((TypeRefImpl) reference).isCollectionType()) {
            lookup = ((DatumType) lookup).getSequenceOfType();
        }
        return lookup;
    }

    public static Set<DatumType> resolveAstPathQuery(List<Directory> dirChain, TypeRefImpl reference) {
        MetaReference parentRef = getParentRef(reference);
        NamedElement spcDir = resolveAstPath(dirChain, parentRef);
        return queryAst(spcDir, new Executor.QueryAstConsumer(ComplexType.class));
    }

    private static MetaReference getParentRef(TypeRefImpl origRef) {
        MetaReference parentRefCopy = new MetaReference();
        parentRefCopy.setFirstPart(origRef.getFirstPart().copy(parentRefCopy));
        MetaRefPart currPartCopy = parentRefCopy.getFirstPart();
        for (MetaRefPart origRefPart : origRef.getPartIterable()) {
            currPartCopy.setNextRefPart(origRefPart.copy(parentRefCopy));
            currPartCopy = currPartCopy.getNextRefPart();
        }
        return parentRefCopy;
    }

    private static NamedElement resolveFromRoot(List<Directory> dirChain, MetaReference reference) {
        NamedElement lookup = findAsDirRoot(dirChain, reference.getFirstPart().getNamePartExpr());
        lookup = traverseRest(lookup, reference.getFirstPart().getNextRefPart());
        return lookup;
    }

    private static NamedElement resolveFromNode(NamedElement astNode, MetaReference reference) {
        log.debug("trying to find ["+reference+"] under [" + astNode.getFQName() + "]");
        NamedElement lookup = lookupImmediateChild(astNode, reference.getFirstPart().getNamePartExpr().getNameExpr());
        lookup = traverseRest(lookup, reference.getFirstPart().getNextRefPart());
        return lookup;
    }

    /** Scans dir chain. */
    public static NamedElement findAsDirRoot(List<Directory> dirChain, NamePartExpr namePartExpr) {
        NamedElement lookup = null;
        for (Directory dir : dirChain) {
            log.debug("trying to find [" + namePartExpr + "] under [" + dir.getFQName() + "]");
            lookup = lookupImmediateChild(dir, namePartExpr.getNameExpr());
            if (lookup != null)
                break;
            else
                log.debug("could not find [" + namePartExpr + "] under [" + dir.getFQName() + "]");
        }
        return lookup;
    }

    public static NamedElement traverseRest(NamedElement context, MetaRefPart refPart) {
        NamedElement targetChild = context;
        if (context != null && refPart != null) {
            targetChild = lookupImmediateChild(context, refPart.getNamePartExpr().getNameExpr());
            if (targetChild != null) {
                if (refPart.hasNextExpr()) {
                    targetChild = traverseRest(targetChild, refPart.getNextRefPart());
                }
            }
        }
        // if not first of path list and child not found under current context, return null (error)
        return targetChild;
    }

    public static NamedElement findInNearestScope(ModelElement context, MetaReference reference,
                                                  ScopeKind containerScopeKind)
    {
        if (containerScopeKind == null)
            containerScopeKind = inferScopeKind(context);
        NamedElement lookup = lookupImmediateChild(context, reference.getFirstPart().getNamePartExpr().getNameExpr());
        if (lookup != null)
            reference.setResolvedDatumScope(containerScopeKind);
        else {
            if (context instanceof StatementBlock) {
                if (context.hasParent()) {
                    // not found so just go up the basic tree
                    ModelElement parent = context.getParent();
                    if (parent instanceof StatementBlock)
                        lookup = findInNearestScope(parent, reference, ScopeKind.BLOCK);
                    else if (parent instanceof SpaceFunctionDefn)
                        lookup = findInNearestScope(parent, reference, ScopeKind.ARG);
                }
            }
            else if (context instanceof FunctionDefn) {
                lookup = findInNearestScope((ModelElement) ((FunctionDefn) context).getArgSpaceTypeDefn(), reference,
                                            ScopeKind.ARG);
                if (lookup == null)
                    lookup = findInNearestScope(context.getParent(), reference, ScopeKind.OBJECT);
            }
        }
        return lookup;
    }

    private static ScopeKind inferScopeKind(ModelElement context) {
        ScopeKind scopeKind = null;
        if (context instanceof StatementBlock
            /* && context.getNamedParent() instanceof FunctionDefn */ )
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof SpaceFunctionDefn)
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof SpaceTypeDefn)
            scopeKind = ScopeKind.OBJECT;

        return scopeKind;
    }

    /** Will traverse into child grouping nodes and grouping nodes only. */
    public static NamedElement lookupImmediateChild(ModelElement context, String name) {
        NamedElement childByName = context.getChildByName(name);
        if (childByName == null) {
            if (context.hasGroupingNodes()) {
                for (ModelElement groupElement : context.getGroupingNodes()) {
                    childByName = lookupImmediateChild(groupElement, name);
                    if (childByName != null)
                        break;
                }
            }
        }
        return childByName;
    }

    private static NamedElement checkIntrinsics(NamePartExpr namePartExpr) {
        NamedElement lookup = null;
        if (namePartExpr.getNameExpr().equals(VoidType.VOID.getName())) {
            lookup = VoidType.VOID;
        }
        else{
            lookup = NumPrimitiveTypeDefn.valueOf(namePartExpr.getNameExpr());
        }
        return lookup;
    }

    public static void walkAst(ModelElement astNode, AstConsumer astAction) {
        boolean match = astAction.getFilter() == null || astAction.getFilter().test(astNode);
        if (match)
            astAction.upon(astNode);
        List<ModelElement> children = astNode.getChildren();
        for (ModelElement child : children) {
            walkAst(child, astAction);
        }
        if (match)
            astAction.after(astNode);
    }

    public static String print(ModelElement modelElement) {
        PrintAstConsumer astPrinter = new PrintAstConsumer();
        walkAst(modelElement, astPrinter);
        return astPrinter.getSb().toString();
    }

    public static boolean contains(Directory parentDir, String name) {
        return getSubDirByName(parentDir, name) != null;
    }

    public static Directory getSubDirByName(Directory parentDir, String name) {
        Directory subDir = null;
        if (parentDir.hasChildDirs()) {
            for (Directory directory : parentDir.getChildDirectories()) {
                if (directory.getName().equals(name)) {
                    subDir = directory;
                    break;
                }
            }
        }
        return subDir;
    }

    public static <T extends ModelElement> Set<T> queryAst(ModelElement queryContextElem, Executor.QueryAstConsumer<T> astQueryAction) {
        walkAst(queryContextElem, astQueryAction);
        return astQueryAction.getResults();
    }

    public static Directory ensureDir(Directory nsRootDir, String[] classNameParts, int numParts) {
        if (!nsRootDir.isRootDir())
            throw new IllegalArgumentException("specified dir must be a namespace root: ["+nsRootDir+"]");

        Directory leafDir = nsRootDir;
        for (int idxName = 0; idxName < numParts - 1; idxName++) {
            String name = classNameParts[idxName];
            if (contains(leafDir, name)) {
                leafDir = leafDir.getChildDir(name);
            }
            else {
                leafDir = leafDir.addDir(SpaceHome.getAstFactory().newAstDir(new ProgSourceInfo(), name));
                log.trace("created new Space dir ["+leafDir+"]");
            }
        }

        return leafDir;
    }

    private static class PrintAstConsumer implements AstConsumer {

        private final StringBuilder sb = new StringBuilder();

        public PrintAstConsumer() {
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return null;
        }

        @Override
        public void upon(ModelElement astNode) {
            String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
            sb.append(JavaHelper.EOL)
              .append(indent).append("(").append(astNode.toString());
        }

        @Override
        public void after(ModelElement astNode) {
            if (astNode.hasChildren()) {
                String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
                sb.append(JavaHelper.EOL).append(indent);
            }
            sb.append(")");
        }

        public StringBuilder getSb() {
            return sb;
        }
    }
}
