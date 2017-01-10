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
public class RaTreePrinter implements RaTreeListener {

    private StringBuilder sb = new StringBuilder();

    public StringBuilder getSb() {
        return sb;
    }

    @Override
    public void startNode(Tree treeContext, List<String> ruleNameIndex, int level) {
        String indent = Strings.multiplyString("\t", level);
        String nodeText = Utils.escapeWhitespace(getNodeTypeInfo(treeContext, ruleNameIndex), false);
        boolean isTerminalNode = treeContext.getChildCount() == 0;

        if (level > 0)
            sb.append("\n");

        sb.append(indent + "{");

        sb.append(nodeText);

        return;
    }

    @Override
    public void endNode(Tree treeContext, List<String> ruleNameIndex, int level) {
        String indent = Strings.multiplyString("\t", level);
        boolean isTerminalNode = treeContext.getChildCount() == 0;
        if (isTerminalNode) {
        }
        else {
            sb.append("\n");
            sb.append(indent);
        }
        sb.append("}");
    }

    private String getNodeTypeInfo(Tree treeContext, List<String> ruleNameIndex) {
        String dumpString = "?";
        if (ruleNameIndex != null) {
            if (treeContext instanceof RuleContext) {
                RuleContext ruleContext = (RuleContext) treeContext;
                int ruleIndex = ruleContext.getRuleIndex();
                String ruleName = ruleNameIndex.get(ruleIndex);
                int altNumber = ruleContext.getAltNumber();
                dumpString = toDumpString(
                        "rule-type",
                        ruleName + ((altNumber != ATN.INVALID_ALT_NUMBER) ? (":" + altNumber) : "")
                );
            }
            else if (treeContext instanceof TerminalNode) {
                Token symbol = ((TerminalNode) treeContext).getSymbol();
                if (symbol != null) {
                    dumpString = toDumpString("terminal", symbol.getText());
                }
            }
            else if (treeContext instanceof ErrorNode) {
                dumpString = toDumpString("error", treeContext.toString());
            }
        }
        else {
            // no recog for rule names
            Object payload = treeContext.getPayload();
            if (payload instanceof Token) {
                dumpString = toDumpString("?", ((Token) payload).getText());
            }
            else {
                dumpString = toDumpString("?", treeContext.getPayload().toString());
            }
        }
        return dumpString;
    }

    private String toDumpString(String typeOfNode, String stringRep) {
        return "[" + typeOfNode + "]='" + stringRep + "'";
    }

}
