/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.runtime.g1.antlr;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;
import org.jkcsoft.java.util.Strings;

import java.util.List;

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
