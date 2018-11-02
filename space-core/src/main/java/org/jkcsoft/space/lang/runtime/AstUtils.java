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

import java.util.HashSet;
import java.util.Iterator;
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
     * <p>Entry point for resolving static references by the linker prior to execution.
     * Rules for resolution vary depending on the type of object referenced:
     * <br>
     * Valid paths by meta type:
     *
     *  Type
     *  Type.Type[... Type]
     *  dir.dir.dir.Type
     *
     *  Datum
     *  this.Datum
     *  Type.Datum (static)
     *  dir.dir.Type.Datum
     *  Func.Datum
     *
     *  Func
     *  dir.dir.Type.Func
     *  Type.Func
     *  this.Func
     *  Func.Func
     *
     * <p>Ref -> (all meta types)
     * <p>
     *    1. Look for first part<br>
     *    1.1 In current parse unit<br>
     *    1.2 In current directory<br>
     *    1.3 In implicit import dir<br>
     *    1.4 In explicit import sequence<br>
     *
     * <p>Ref -> Type: must be either fully qualified, possibly via an 'import', or unqualified.
     * <p>Multi-part Type ref must be [dir. ...]dir.Type[.Type]
     *     - Linker:<br>
     *    2. Otherwise resolve FQ ref via imports.<br>
     *         Then locate from root of AST.
     * <p>
     * - Exe: (no need)
     *
     * <p>Ref -> Datum:
     *  - Linker: If first node in path (including a 1-length path), resolve
     *  according to this search order (precedence):
     * <p>
     *  1. block local -> parent blocks(s)
     *  2. function args (if ref is inside function body)
     *  3. object member datums
     *  4. static/shared datums, fully qualified
     *<p>
     *    If not first in chain, resolve with respect to type of preceding object.
     *<p>
     *  - Exe: resolves to value or ref.
     *
     * <p>Ref -> Function:
     * <p>
     *  1. "context object" member functions
     *  2. static functions, fully qualified
     *
     * @param reference
     * @return
     */
    public static void resolveAstPath(ExpressionChain reference) {
        Namespace refNs = getNs(reference);

        switch (reference.getTargetMetaType()) {
            case TYPE:
                if (reference.isSinglePart()) {
                    resolveIntrinsics(((SimpleExprLink) reference.getFirstPart()));
                    // check for 'siblings': in same parse unit or directory
                    if (reference.isAtInitState()) {
                        ParseUnit parentParseUnit = findParentParseUnit(reference, parseUnitFinderAction);
                        resolveFromNode(parentParseUnit, reference);
                        if (reference.isAtInitState()) {
                            Directory parentDir = findParentDir(reference, dirFinderAction);
                            resolveFromNode(parentDir, reference);
                        }
                    }
                }
                // check as full path from root
                if (reference.isAtInitState()) {
                    resolveFromRoot(refNs.getRootDirLookupChain(), reference);
                }
                break;
            case DATUM:
                resolveInNearestScope(AstUtils.getNearestScopeParent(reference), reference, null);
                if (reference.isAtInitState()) {
                    resolveFromRoot(refNs.getRootDirLookupChain(), reference);
                    if (reference.isResolved())
                        reference.setResolvedDatumScope(ScopeKind.STATIC);
                }
                break;
            case FUNCTION:
//                resolveFromRoot(refNs.getRootDirLookupChain(), reference);
                if (reference.isAtInitState() ) {
                    if (!reference.isMultiPart()) {
                        resolveInNearestScope(AstUtils.getNearestScopeParent(reference), reference, null);
                    }
                    else {
                        resolveFromNode(reference.getNamedParent(), reference);
                    }
                }
                break;
        }

    }

    static void checkSetResolve(ExprLink refPart, NamedElement lookup) {
        if (lookup != null) {
            refPart.setResolvedMetaObj(lookup);
            refPart.setState(LinkState.RESOLVED);
        }
    }

    public static Namespace getNs(ExpressionChain reference) {
        Namespace ns = null;
        if (reference.hasNs())
            ns = SpaceHome.getNsRegistry().getNamespace(reference.getNsRefPart().getNameExprText());
        else
            ns = AstUtils.findParentNs(reference);

        return ns;
    }

    private static Namespace findParentNs(ModelElement astNode) {
        return findFirstParent(astNode, nsFinderAction);
    }

    public static <T extends ModelElement> T findFirstParent(ModelElement astNode, Executor.FindFirstAstConsumer<T> finderAction) {
        finderAction.clearResult();
        AstUtils.iterateParents(astNode, finderAction);
        return finderAction.getResult();
    }

    public static Directory findParentDir(ModelElement astNode, Executor.FindFirstAstConsumer<Directory> finderAction) {
        return findFirstParent(astNode, finderAction);
    }

    public static ParseUnit findParentParseUnit(ModelElement astNode, Executor.FindFirstAstConsumer<ParseUnit> finderAction) {
        return findFirstParent(astNode, finderAction);
    }

    public static Set<DatumType> getSiblingTypes(TypeRefImpl reference) {
        ExprLink parentRefPart = getParentRefPart(reference);
        return getChildTypes((Directory) parentRefPart.getResolvedMetaObj());
    }

    public static Set<DatumType> getChildTypes(Directory dir) {
        Set<DatumType> childTypes = new HashSet<>();
        dir.getParseUnits().forEach(parseUnit -> childTypes.addAll(parseUnit.getTypeDefns()));
        return childTypes;
    }

    private static ExprLink getParentRefPart(TypeRefImpl fullRef) {
        List<ExprLink> pathParts = fullRef.getExprLinks();
        return pathParts.get(pathParts.size() - 2);
    }

    private static void resolveFromRoot(Directory[] dirChain, ExpressionChain reference) {
        for (Directory rootDir : dirChain) {
            log.debug("trying to find [" + reference + "] under [" + rootDir.getFQName() + "]");
            resolveRest(rootDir, reference.getExprLinks().iterator());
            if (reference.isResolved()) {
                break;
            }
            else {
                log.debug("could not find [" + reference + "] under [" + rootDir.getFQName() + "]");
            }
        }
    }

    private static void resolveFromNode(ModelElement astNode, ExpressionChain reference) {
        log.debug("trying to find ["+reference+"] under [" + astNode + "]");
        NamedElement lookup =
            lookupImmediateChild(astNode, (((SimpleExprLink) reference.getFirstPart()).getNameExprText()));
        checkSetResolve(reference.getFirstPart(), lookup);
        resolveRest(lookup, reference.getExprLinks().iterator());
    }

    public static void resolveRest(NamedElement context, Iterator<ExprLink> refPartsIter) {
        if (context != null && refPartsIter.hasNext()) {
            SimpleExprLink refPart = (SimpleExprLink) refPartsIter.next();
            NamedElement targetChild = lookupImmediateChild(context, refPart.getExpression().getNameExpr());
            checkSetResolve(refPart, targetChild);
            if (refPart.isResolved()) {
                if (refPartsIter.hasNext()) {
                    resolveRest((NamedElement) refPart.getResolvedMetaObj(), refPartsIter);
                }
            }
        }
    }

    public static void resolveInNearestScope(ModelElement context, ExpressionChain reference,
                                                     ScopeKind containerScopeKind)
    {
        if (containerScopeKind == null)
            containerScopeKind = inferScopeKind(context);
        NamedElement lookup = lookupImmediateChild(context, ((SimpleExprLink) reference.getFirstPart()).getNameExprText());
        checkSetResolve(reference.getFirstPart(), lookup);
        if (reference.isResolved())
            reference.setResolvedDatumScope(containerScopeKind);
        else {
            if (context instanceof StatementBlock) {
                if (context.hasParent()) {
                    // not found so just go up the basic tree
                    ModelElement parent = context.getParent();
                    if (parent instanceof StatementBlock)
                        resolveInNearestScope(parent, reference, ScopeKind.BLOCK);
                    else if (parent instanceof SpaceFunctionDefn)
                        resolveInNearestScope(parent, reference, ScopeKind.ARG);
                }
            }
            else if (context instanceof FunctionDefn) {
                resolveInNearestScope((ModelElement) ((FunctionDefn) context).getArgSpaceTypeDefn(), reference,
                                               ScopeKind.ARG);
                if (reference.isAtInitState())
                    resolveInNearestScope(context.getParent(), reference, ScopeKind.OBJECT);
            }
        }
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

    private static void resolveIntrinsics(SimpleExprLink refPart) {
        NamedElement lookup = null;
        if (refPart.getExpression().getNameExpr().equals(VoidType.VOID.getName())) {
            lookup = VoidType.VOID;
        }
        else {
            lookup = NumPrimitiveTypeDefn.valueOf(refPart.getExpression().getNameExpr());
        }
        checkSetResolve(refPart, lookup);
    }

    public static void walkAstDepthFirst(ModelElement astNode, AstScanConsumer astAction) {
        boolean match = astAction.getFilter() == null || astAction.getFilter().test(astNode);
        if (match)
            astAction.upon(astNode);
        List<ModelElement> children = astNode.getChildren();
        for (ModelElement child : children) {
            walkAstDepthFirst(child, astAction);
        }
        if (match)
            astAction.after(astNode);
    }

    public static void iterateParents(ModelElement astNode, AstScanConsumer astAction) {
        boolean match = astAction.getFilter() == null || astAction.getFilter().test(astNode);
        if (match)
            astAction.upon(astNode);

        if (astNode.hasParent())
            iterateParents(astNode.getParent(), astAction);

        if (match)
            astAction.after(astNode);
    }

    public static String print(ModelElement modelElement) {
        PrintAstConsumer astPrinter = new PrintAstConsumer();
        walkAstDepthFirst(modelElement, astPrinter);
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
        walkAstDepthFirst(queryContextElem, astQueryAction);
        return astQueryAction.getResults();
    }

    public static Directory ensureDir(Directory nsRootDir, String[] classNameParts) {
        if (!nsRootDir.isRootDir())
            throw new IllegalArgumentException("specified dir must be a namespace root: ["+nsRootDir+"]");

        Directory leafDir = nsRootDir;
        for (int idxName = 0; idxName < classNameParts.length; idxName++) {
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

    public static boolean isJavaNs(ImportExpr importExpr) {
        SimpleExprLink<Namespace> nsRefPart = importExpr.getTypeRefExpr().getNsRefPart();
        return nsRefPart != null
            && nsRefPart.getNameExprText().equals(SpaceHome.getNsRegistry().getJavaNs().getName());
    }

    public static void addNewMetaRefParts(ExpressionChain parentPath, SourceInfo sourceInfo, String... nameExprs) {
        for (String nameExpr : nameExprs) {
            parentPath.addNextPart(SpaceHome.getAstFactory().newMetaRefPart(sourceInfo, nameExpr));
        }
        return;
    }

    private static Executor.FindFirstAstConsumer<Directory> dirFinderAction =
        new Executor.FindFirstAstConsumer<>(Directory.class);

    private static Executor.FindFirstAstConsumer<ParseUnit> parseUnitFinderAction =
        new Executor.FindFirstAstConsumer<>(ParseUnit.class);

    private static Executor.FindFirstAstConsumer<Namespace> nsFinderAction =
        new Executor.FindFirstAstConsumer<>(Namespace.class);

    /** Of the two specified arg types, returns the 'largest' of the two. */
    private static DatumType[][] typePrecedenceMap = new DatumType[][] {
        {NumPrimitiveTypeDefn.REAL, NumPrimitiveTypeDefn.CARD}
    };
    public static DatumType larger(DatumType type1, DatumType type2) {
        DatumType larger = null;
//        for (DatumType[] typePair : typePrecedenceMap) {
//            if typePair[0] = type1
//        }
        return larger;
    }

    private static class PrintAstConsumer implements AstScanConsumer {

        private final StringBuilder sb = new StringBuilder();

        public PrintAstConsumer() {
        }

        @Override
        public Predicate<ModelElement> getFilter() {
            return null;
        }

        @Override
        public boolean upon(ModelElement astNode) {
            String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
            sb.append(JavaHelper.EOL)
              .append(indent).append("(").append(astNode.toString());
            return true;
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
