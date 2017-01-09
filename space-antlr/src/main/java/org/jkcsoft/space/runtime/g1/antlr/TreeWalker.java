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
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.SpaceParser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Walks the Space Antlr tree and fires events to any listeners.  Used to
 * write pretty-print of the tree and also to build our AST.
 *
 * @author Jim Coles
 */
public class TreeWalker {

    private List<TreeListener> listeners = new LinkedList<>();

    public void addListener(TreeListener listener) {
        listeners.add(listener);
    }

    private void buildAst(ParseTree tree, SpaceParser spaceParser, StringBuilder sbDump) {
        String[] ruleNames = spaceParser != null ? spaceParser.getRuleNames() : null;
        List<String> ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
        walkAndBuild(tree, ruleNamesList, sbDump, 0);
    }

    private void walkAndBuild(Tree treeContext, List<String> ruleNamesList, StringBuilder sb, int level) {
        String indent = Strings.multiplyString("\t", level);
        String indentMore = Strings.multiplyString("\t", level+1);
        String nodeText = Utils.escapeWhitespace(getNodeTypeInfo(treeContext, ruleNamesList), false);
        boolean isTerminalNode = treeContext.getChildCount() == 0;

        if (level > 0)
            sb.append("\n");

        sb.append(indent + "{");

        sb.append(nodeText);

        if (!isTerminalNode) {
            level++;
            for (int i = 0; i < treeContext.getChildCount(); i++) {
                if (i > 0) sb.append(' ');
                walkAndBuild(treeContext.getChild(i), ruleNamesList, sb, level);
            }
        }
        if (isTerminalNode) {
            sb.append("}");
        }
        else {
            sb.append("\n");
            sb.append(indent);
            sb.append("}");
        }
        return;
    }

    private String getNodeTypeInfo(Tree treeContext, List<String> ruleNameIndex) {
        String dumpString = "?";
        if (ruleNameIndex != null) {
            if (treeContext instanceof RuleContext) {
                int ruleIndex = ((RuleContext) treeContext).getRuleContext().getRuleIndex();
                String ruleName = ruleNameIndex.get(ruleIndex);
                int altNumber = ((RuleContext) treeContext).getAltNumber();
                dumpString = toDumpString(
                        "rule-type",
                        ruleName + ((altNumber != ATN.INVALID_ALT_NUMBER) ? (":" + altNumber) : "")
                );
            } else if (treeContext instanceof ErrorNode) {
                dumpString = toDumpString("error", treeContext.toString());
            } else if (treeContext instanceof TerminalNode) {
                Token symbol = ((TerminalNode) treeContext).getSymbol();
                if (symbol != null) {
                    dumpString = toDumpString("terminal", symbol.getText());
                }
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
        return "["+typeOfNode+"]='" + stringRep + "'";
    }

}
