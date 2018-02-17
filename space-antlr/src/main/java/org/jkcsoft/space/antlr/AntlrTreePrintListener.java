/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.*;

/**
 * @author Jim Coles
 */
public class AntlrTreePrintListener implements ParseTreeListener {

    private StringBuilder sb = new StringBuilder();
    private String[] ruleIndex;
    private boolean lastExitTerm = false;

    public AntlrTreePrintListener(String[] ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    public StringBuilder getSb() {
        return sb;
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        String indent = AntlrUtil.indent(ctx);
        String nodeText = Utils.escapeWhitespace(AntlrUtil.toDumpString(ctx, ruleIndex[ctx.getRuleIndex()]), false);

        if (ctx.depth() > 1)
            sb.append("\n");

        sb.append(indent + "{");

        sb.append(nodeText);

        return;

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        String indent = AntlrUtil.indent(ctx);
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

    @Override
    public void visitTerminal(TerminalNode node) {
        sb.append("\n" + AntlrUtil.indent(((ParserRuleContext) node.getParent()).depth() + 1));
        sb.append(AntlrUtil.toDumpString(node));
        lastExitTerm = true;
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        sb.append(AntlrUtil.toDumpString(node));
    }


}
