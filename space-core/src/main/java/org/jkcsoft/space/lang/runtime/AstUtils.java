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
import org.jkcsoft.space.lang.ast.*;

import java.util.List;

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

    public static Schema getRunSchema(Schema thisSchema) {
        Schema schema =
            (Schema) CollectionUtils.find(thisSchema.getChildren(),
                                          object -> object instanceof Schema);
        return schema;
    }

    public static Schema getLangRoot(List<Schema> dirChain) {
        return dirChain.get(0);
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
    public static NamedElement resolveAstPath(List<Schema> dirChain, MetaReference reference)
    {
        NamedElement lookup = null;
        switch (reference.getTargetMetaType()) {
            case TYPE:
                lookup = checkIntrinsics(reference.getSpacePathExpr());
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
                if (!reference.getSpacePathExpr().hasNextExpr())
                    lookup = findInNearestScope(AstUtils.getNearestScopeParent(reference), reference, null);
                else
                    lookup = resolveFromRoot(dirChain, reference);
                break;
        }
        return lookup;
    }

    private static NamedElement resolveFromRoot(List<Schema> dirChain, MetaReference reference) {
        NamedElement lookup;
        lookup = findAsSchemaRoot(dirChain, reference.getSpacePathExpr());
        if (reference.getSpacePathExpr().hasNextExpr()) {
            lookup = traverseRest(lookup, reference.getSpacePathExpr().getNextExpr());
        }
        return lookup;
    }

    /** Scans dir chain. */
    public static NamedElement findAsSchemaRoot(List<Schema> dirChain, SpacePathExpr spacePathExpr) {
        NamedElement lookup = null;
        for (ModelElement tryContext : dirChain) {
            log.debug("trying to find [" + spacePathExpr + "] under schema [" + tryContext + "]");
            lookup = lookupImmediateChild(tryContext, spacePathExpr.getNodeText());
            if (lookup != null)
                break;
            else
                log.debug("could not find [" + spacePathExpr + "] under [" + tryContext + "]");
        }
        return lookup;
    }

    public static NamedElement traverseRest(ModelElement context, SpacePathExpr spacePathExpr) {
        NamedElement targetChild = lookupImmediateChild(context, spacePathExpr.getNodeText());
        if (targetChild != null) {
            if (spacePathExpr.hasNextExpr()) {
                targetChild = traverseRest(targetChild, spacePathExpr.getNextExpr());
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
        NamedElement lookup = lookupImmediateChild(context, reference.getSpacePathExpr().getNodeText());
        if (lookup != null)
            reference.setResolvedDatumScope(containerScopeKind);
        else {
            if (context instanceof StatementBlock) {
                if (reference.getSpacePathExpr().isFirst() && context.hasParent()) {
                    // not found so just go up the basic tree
                    ModelElement parent = context.getParent();
                    if (parent instanceof StatementBlock)
                        lookup = findInNearestScope(parent, reference, ScopeKind.BLOCK);
                    else if (parent instanceof FunctionDefn)
                        lookup = findInNearestScope(parent, reference, ScopeKind.ARG);
                }
            }
            else if (context instanceof FunctionDefn) {
                lookup = findInNearestScope(((FunctionDefn) context).getArgSpaceTypeDefn(), reference, ScopeKind.ARG);
                if (lookup == null)
                    lookup = findInNearestScope(((FunctionDefn) context).getParent(), reference, ScopeKind.OBJECT);
            }
        }
        return lookup;
    }

    private static ScopeKind inferScopeKind(ModelElement context) {
        ScopeKind scopeKind = null;
        if (context instanceof StatementBlock
            /* && context.getNamedParent() instanceof FunctionDefn */ )
            scopeKind = ScopeKind.BLOCK;
        else if (context instanceof FunctionDefn)
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

    private static NamedElement checkIntrinsics(SpacePathExpr spacePathExpr) {
        NamedElement lookup = null;
        if (spacePathExpr.getNodeText().equals(VoidType.VOID.getName())) {
            lookup = VoidType.VOID;
        }
        else{
            lookup = PrimitiveTypeDefn.valueOf(spacePathExpr.getNodeText());
        }
        return lookup;
    }

    public static String print(ModelElement modelElement) {
        StringBuilder sb = new StringBuilder();
        appendPrint(sb, modelElement, 0);
        return sb.toString();
    }

    private static void appendPrint(StringBuilder sb, ModelElement modelElement, int depth) {
        String indent = Strings.multiplyString("\t", depth);
        sb.append(JavaHelper.EOL)
          .append(indent).append("(").append(modelElement.toString());
        List<ModelElement> children = modelElement.getChildren();
        for (ModelElement child : children) {
            appendPrint(sb, child, depth + 1);
        }

        if (children.size() > 0)
            sb.append(JavaHelper.EOL).append(indent);

        sb.append(")");
    }
}
