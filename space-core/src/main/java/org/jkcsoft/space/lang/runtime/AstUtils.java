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
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.ModelElement;
import org.jkcsoft.space.lang.ast.NamedElement;
import org.jkcsoft.space.lang.ast.Schema;

import java.util.List;

/**
 * Utility methods for querying and traversing the Space AST, which
 * can also be thought of as a directory of nodes.
 *
 * @author Jim Coles
 */
public class AstUtils {

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
                .append(indent)
                .append("(")
                .append(modelElement.toString());
        List<ModelElement> children = modelElement.getChildren();
        for (ModelElement child : children) {
            append(sb, child, depth + 1);
        }
        sb.append(JavaHelper.EOL)
                .append(indent)
                .append(")");
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
}
