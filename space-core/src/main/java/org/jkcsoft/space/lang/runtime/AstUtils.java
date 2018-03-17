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

import java.util.LinkedList;
import java.util.List;

/**
 * Utility methods for querying and traversing the Space AST, which can also be thought of as a directory of nodes.
 *
 * @author Jim Coles
 */
public class AstUtils {

    private static final Logger log = Logger.getLogger(AstUtils.class);

    public static NamedElement getLexParent(ModelElement anElem) {
        NamedElement named = null;
        if (anElem.getParent() instanceof NamedElement)
            named = (NamedElement) anElem.getParent();
        else if (anElem.getParent() != null) {
            named = getLexParent(anElem.getParent());
        }
//        if (named == null)
        return named;
    }

    public static String print(ModelElement modelElement) {
        StringBuilder sb = new StringBuilder();
        append(sb, modelElement, 0);
        return sb.toString();
    }

    private static void append(StringBuilder sb, ModelElement modelElement, int depth) {
        String indent = Strings.multiplyString("\t", depth);
        sb.append(JavaHelper.EOL)
          .append(indent).append("(").append(modelElement.toString());
        List<ModelElement> children = modelElement.getChildren();
        for (ModelElement child : children) {
            append(sb, child, depth + 1);
        }
        sb.append(JavaHelper.EOL)
          .append(indent).append(")");
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
        return child.isGroupingNode() ||
        child.getClass().getAnnotation(GroupingNode.class) != null;
    }

    public static NamedElement lookupLenientMetaObject(List<Schema> dirChain, ModelElement context,
                                                       SpacePathExpr spacePathExpr)
    {
        NamedElement lookup = checkIntrinsics(spacePathExpr);
        if (lookup == null) {
            List<ModelElement> trySequence = new LinkedList<>();
            trySequence.add(context);
            trySequence.addAll(dirChain);
            for (ModelElement tryContext : trySequence) {
                log.debug("trying to find [" + spacePathExpr + "] under [" + tryContext + "]");
                lookup = lookupMetaObject(tryContext, spacePathExpr, true);
                if (lookup != null)
                    break;
                else
                    log.debug("could not find [" + spacePathExpr + "] under [" + tryContext + "]");
            }
        }
        return lookup;
    }

    private static NamedElement checkIntrinsics(SpacePathExpr spacePathExpr) {
        NamedElement lookup = null;
        if (spacePathExpr.getText().equals(VoidType.VOID.getName())) {
            lookup = VoidType.VOID;
        }
        else{
            lookup = PrimitiveTypeDefn.valueOf(spacePathExpr.getText());
        }
        return lookup;
    }

    public static NamedElement lookupMetaObject(ModelElement context, SpacePathExpr spacePathExpr, boolean isRoot) {
//        Executor.log.debug("lookup meta object from " + context.getName() + " -> " + spacePathExpr );
        NamedElement targetChild = lookupMetaObject(context, spacePathExpr.getText());
        if (targetChild != null) {
            if (spacePathExpr.hasNextExpr()) {
                targetChild = lookupMetaObject(targetChild, spacePathExpr.getNextExpr(), false);
            }
        }
        // if not root and child not found under current context, return null (error)
        else if (isRoot && context.hasParent()) {
            targetChild = lookupMetaObject(context.getParent(), spacePathExpr.getText());
        }
        return targetChild;
    }

    /** Will traverse into child grouping nodes */
    public static NamedElement lookupMetaObject(ModelElement context, String name) {
        NamedElement childByName = context.getChildByName(name);
        if (childByName == null) {
            if (context.hasGroupingNodes()) {
                for (ModelElement groupElement : context.getGroupingNodes()) {
                    childByName = lookupMetaObject(groupElement, name);
                    if (childByName != null)
                        break;
                }
            }
//            Executor.log.warn("element [" + context + "] does not contain named child [" + name + "]");
        }
        return childByName;
    }
}
