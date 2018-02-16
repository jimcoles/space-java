/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.tool.ast.GrammarAST;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.runtime.loaders.antlr.g2.SimpleTransListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AntlrUtil {

    private static final List<String> tabsCache = new ArrayList<>();

    public static String getNodeTypeInfo(Tree treeContext, String[] ruleNameIndex) {
        String dumpString = "?";
        if (ruleNameIndex != null) {
            if (treeContext instanceof RuleContext) {
                RuleContext ruleContext = (RuleContext) treeContext;
                dumpString = toDumpString(ruleContext, ruleNameIndex[ruleContext.getRuleIndex()]);
            } else if (treeContext instanceof TerminalNode) {
                if (treeContext instanceof ErrorNode) {
                    ErrorNode errorNode = (ErrorNode) treeContext;
                    dumpString = toDumpString(errorNode);
                }
                else {
                    TerminalNode terminalNode = (TerminalNode) treeContext;
                    dumpString = toDumpString(terminalNode);
                }
            }
        } else {
            // no recog for rule names
            Object payload = treeContext.getPayload();
            if (payload instanceof Token) {
                dumpString = toDumpString("?", ((Token) payload).getText());
            } else {
                dumpString = toDumpString("?", treeContext.getPayload().toString());
            }
        }
        return dumpString;
    }

    public static String toDumpString(ErrorNode errorNode) {
        String dumpString;
        dumpString = toDumpString("error", errorNode.toString());
        return dumpString;
    }

    public static String toDumpString(TerminalNode terminalNode) {
        String dumpString = "";
        Token symbol = terminalNode.getSymbol();
        if (symbol != null) {
            dumpString = toDumpString("terminal", symbol.getText());
        }
        return dumpString;
    }

    public static String toDumpString(RuleContext ruleContext, String ruleName) {
        return toDumpString(
                "rule-type",
                ruleName +
                        ((ruleContext.getAltNumber() != ATN.INVALID_ALT_NUMBER) ?
                                (":" + ruleContext.getAltNumber()) : ""));
    }

    public static String toDumpString(String typeOfNode, String stringRep) {
        return "[" + typeOfNode + "]='" + stringRep + "'";
    }

    public static String indent(ParserRuleContext ctx) {
        return indent(ctx.depth());
    }

    public static String indent(int depthOneBased) {
        String tabs = null;
        if (tabsCache.size() < depthOneBased) {
            fillCache(depthOneBased);
        }
        tabs = tabsCache.get(depthOneBased - 1);
        return tabs;
    }

    private static void fillCache(int depthOneBased) {
        for (int idxTab = tabsCache.size(); idxTab < depthOneBased; idxTab++) {
            tabsCache.add(Strings.multiplyString("\t", idxTab));
        }
    }

    /** The ANTLR rule name is always the first child of the rule node. */
    public static String extractRuleName(GrammarAST rule) {
        return rule.getChild(0).getText();
    }
}
