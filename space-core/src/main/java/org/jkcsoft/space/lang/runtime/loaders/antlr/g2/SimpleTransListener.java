/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr.g2;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.AstFactory;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * As the raw ANTLR rules are encountered,
 * <p>
 * 1. Coalesce if the rules are nested.
 * 2. Look for wrapper AST class.
 *
 * @author Jim Coles
 */
public class SimpleTransListener implements ParseTreeListener {

    private static final Logger log = Logger.getLogger(SimpleTransListener.class);

    // ---------------------------------
    //
    private String[] ruleIndex;
    private SortedMap<Integer, RuleStatInfo> ruleStats = new TreeMap<>();
    private int coalesceDepth = 0;
    private AstFactory spaceAst = new AstFactory();

    // ---------------------------------
    //
    public SimpleTransListener(String[] ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        String ruleName = this.ruleIndex[ctx.getRuleIndex()];
        incRuleTypeCounter(ctx.getRuleIndex());
        String astClassName = Antrl2AstMapping.antlrRuleToAstClassname(ruleName);
        String astClassFQN = Antrl2AstMapping.computeFQAstClassName(astClassName);
        RuleStatInfo statInfo = ruleStats.get(ctx.getRuleIndex());
        statInfo.className = astClassName;
        if (Antrl2AstMapping.shouldCoalesce(ctx.getRuleIndex())) {
            log.debug("coalescing " + ruleName + " at " + ctx.getSourceInterval());
            coalesceDepth++;
        }
        else {
            statInfo.hasAstWrapper = Antrl2AstMapping.canLoad(astClassFQN);
        }
        return;
    }


    private void incRuleTypeCounter(int ruleId) {
        String ruleName = ruleIndex[ruleId];
        RuleStatInfo info = ruleStats.get(ruleId);
        if (info != null) {
            info.counter++;
        } else {
            info = new RuleStatInfo(ruleId, ruleName, null, 1, null);
        }
        ruleStats.put(info.ruleId, info);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        if (coalesceDepth > 0)
                coalesceDepth--;
    }

    @Override
    public void visitTerminal(TerminalNode node) {

    }

    @Override
    public void visitErrorNode(ErrorNode node) {

    }

    public String dumpRuleStats() {
        StringBuffer sb = new StringBuffer();
        ruleStats.keySet().forEach(
                key -> {
                    RuleStatInfo statInfo = ruleStats.get(key);
                    Antrl2AstMapping.RuleMeta ruleMeta = Antrl2AstMapping.getRuleMapping(key);
                    sb.append(statInfo)
                        .append(
                            (statInfo != null && statInfo.hasAstWrapper != null && !statInfo.hasAstWrapper
                             && !(Antrl2AstMapping.shouldCoalesce(key)))
                             ? "****" : ""
                        )
                        .append(JavaHelper.EOL);
                }
        );
        return sb.toString();
    }

    public static class RuleStatInfo {

        public static final String FORMAT = "%3d %-25s %-25s %6d %10b";

        public int ruleId;
        public String ruleName;
        public String className = "";
        public int counter = 0;
        public Boolean hasAstWrapper = null;

        public RuleStatInfo(int ruleId, String ruleName, String className, int counter, Boolean hasAstWrapper) {
            this.ruleId = ruleId;
            this.ruleName = ruleName;
            this.className = className;
            this.counter = counter;
            this.hasAstWrapper = hasAstWrapper;
        }

        @Override
        public String toString() {
            return String.format(FORMAT, ruleId, ruleName, className, counter, hasAstWrapper);
        }
    }

}
