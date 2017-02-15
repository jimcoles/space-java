/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime.loaders.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.*;
import org.jkcsoft.java.util.Strings;

import java.util.List;

/**
 * @author Jim Coles
 */
public class AntlrTreeNodePrinter implements ParseTreeListener {

    private StringBuilder sb = new StringBuilder();
    private List<String> ruleIndex;
    private boolean lastExitTerm = false;

    public AntlrTreeNodePrinter(List<String> ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    public StringBuilder getSb() {
        return sb;
    }

//    private String getNodeTypeInfo(Tree treeContext, List<String> ruleNameIndex) {
//        String dumpString = "?";
//        if (ruleNameIndex != null) {
//            if (treeContext instanceof RuleContext) {
//                RuleContext ruleContext = (RuleContext) treeContext;
//                dumpString = toDumpString(ruleContext, ruleNameIndex);
//            }
//            else if (treeContext instanceof TerminalNode) {
//                TerminalNode terminalNode = (TerminalNode) treeContext;
//                dumpString = toDumpString(dumpString, terminalNode);
//            }
//            else if (treeContext instanceof ErrorNode) {
//                ErrorNode errorNode = (ErrorNode) treeContext;
//                dumpString = toDumpString(errorNode);
//            }
//        }
//        else {
//            // no recog for rule names
//            Object payload = treeContext.getPayload();
//            if (payload instanceof Token) {
//                dumpString = toDumpString("?", ((Token) payload).getText());
//            }
//            else {
//                dumpString = toDumpString("?", treeContext.getPayload().toString());
//            }
//        }
//        return dumpString;
//    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        String indent = indent(ctx);
        String nodeText = Utils.escapeWhitespace(toDumpString(ctx, ruleIndex), false);

        if (ctx.depth() > 1)
            sb.append("\n");

        sb.append(indent + "{");

        sb.append(nodeText);

        return;

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        String indent = indent(ctx);
//        boolean isTerminalNode = ctx.getChildCount() == 0;
        if (lastExitTerm) {
        }
        else {
            sb.append("\n");
            sb.append(indent);
        }
        sb.append("}");
        lastExitTerm = false;
    }

    private String indent(ParserRuleContext ctx) {
        return indent(ctx.depth());
    }

    private String indent(int depthOneBased) {
        return Strings.multiplyString("\t", (depthOneBased - 1));
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        sb.append("\n" + indent(((ParserRuleContext) node.getParent()).depth() + 1));
        sb.append(toDumpString(node));
        lastExitTerm = true;
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        sb.append(toDumpString(node));
    }

    private String toDumpString(ErrorNode errorNode) {
        String dumpString;
        dumpString = toDumpString("error", errorNode.toString());
        return dumpString;
    }

    private String toDumpString(TerminalNode terminalNode) {
        String dumpString = "";
        Token symbol = terminalNode.getSymbol();
        if (symbol != null) {
            dumpString = toDumpString("terminal", symbol.getText());
        }
        return dumpString;
    }

    private String toDumpString(RuleContext ruleContext, List<String> ruleNameIndex) {
        String dumpString;
        int ruleIndex = ruleContext.getRuleIndex();
        String ruleName = ruleNameIndex.get(ruleIndex);
        int altNumber = ruleContext.getAltNumber();
        dumpString = toDumpString(
            "rule-type",
            ruleName + ((altNumber != ATN.INVALID_ALT_NUMBER) ? (":" + altNumber) : "")
        );
        return dumpString;
    }

    private String toDumpString(String typeOfNode, String stringRep) {
        return "[" + typeOfNode + "]='" + stringRep + "'";
    }

}
