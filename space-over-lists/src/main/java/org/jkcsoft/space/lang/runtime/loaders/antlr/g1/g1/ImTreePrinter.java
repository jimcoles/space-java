/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr.g1.g1;

import org.jkcsoft.java.util.Strings;

/**
 * @author Jim Coles
 */
public class ImTreePrinter implements ImTreeListener {

    private StringBuilder sb = new StringBuilder();

    public StringBuilder getSb() {
        return sb;
    }

    @Override
    public void startNode(ImTreeNode node) {
        String indent = Strings.multiplyString("\t", node.getLevel());

        if (node.getLevel() > 0)
            sb.append("\n");

        sb.append(indent + "{");

        sb.append( (node.getType().isList()) ? "(" : "[" + node.getType() + "] " + node.getText());

        return;
    }

    @Override
    public void endNode(ImTreeNode node) {
        String indent = Strings.multiplyString("\t", node.getLevel());
        if (node.getType().isList()) {
            sb.append("\n");
            sb.append(indent);
            sb.append(")");
        }
        sb.append("}");
    }

}
