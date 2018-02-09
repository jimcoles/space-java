/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import org.jkcsoft.space.lang.ast.AstBuilder;

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
    public static final String AST_PACKAGE_NAME = AstBuilder.class.getPackage().getName();

    private static SortedMap<Integer, RuleMeta> ruleMetas = new TreeMap<>();
    static {
        addRuleMeta(SpaceParser.RULE_statement);
        addRuleMeta(SpaceParser.RULE_valueOrAssignmentExpr);
        addRuleMeta(SpaceParser.RULE_expression);
        addRuleMeta(SpaceParser.RULE_anyThing);
        addRuleMeta(SpaceParser.RULE_parameterDecl);
        addRuleMeta(SpaceParser.RULE_valueExpr);
        addRuleMeta(SpaceParser.RULE_valueOrAssignmentExpr);
        addRuleMeta(SpaceParser.RULE_comment);
        addRuleMeta(SpaceParser.RULE_anyTypeRef);
        addRuleMeta(SpaceParser.RULE_primitiveTypeName);
        addRuleMeta(SpaceParser.RULE_literalExpr);
        addRuleMeta(SpaceParser.RULE_scalarLiteral);
    }

    private static RuleMeta addRuleMeta(int ruleId) {
        return ruleMetas.put(ruleId, new RuleMeta(ruleId, true));
    }

    // ---------------------------------
    //
    private String[] ruleIndex;
    private SortedMap<Integer, RuleStatInfo> ruleStats = new TreeMap<>();
    private int coalesceDepth = 0;
    private AstBuilder spaceAst = new AstBuilder();

    // ---------------------------------
    //
    public SimpleTransListener(String[] ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        String ruleName = this.ruleIndex[ctx.getRuleIndex()];
        incRuleTypeCounter(ctx.getRuleIndex());
        String astClassName = mangleClassName(ruleName);
        String astClassFQN = AST_PACKAGE_NAME + "." + astClassName;
        RuleStatInfo info = ruleStats.get(ctx.getRuleIndex());
        info.className = astClassName;
        if (shouldCoalesce(ctx.getRuleIndex())) {
            log.debug("coalescing " + ruleName + " at " + ctx.getSourceInterval());
            coalesceDepth++;
        }
        else {
            info.hasAstWrapper = canLoad(astClassFQN);
        }
        return;
    }

    private boolean shouldCoalesce(int ruleId) {
        RuleMeta ruleMeta = ruleMetas.get(ruleId);
        return ruleMeta != null && ruleMeta.isChoiceRule;
    }

    private boolean canLoad(String astClassFQN) {
        boolean canLoad = false;
        try {
            Class.forName(astClassFQN);
            canLoad = true;
        } catch (ClassNotFoundException e) {
            canLoad = false;
        }
        return canLoad;
    }

    private String mangleClassName(String ruleName) {
        return Character.toUpperCase(ruleName.charAt(0))
                + ruleName.substring(1, ruleName.length());
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
                    RuleMeta ruleMeta = ruleMetas.get(key);
                    sb.append(statInfo)
                        .append(
                            (statInfo != null && statInfo.hasAstWrapper != null && !statInfo.hasAstWrapper
                             && !(shouldCoalesce(key)))
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

    public static class RuleMeta {
        /**
         * True if rule is a choice.
         */
        public Integer ruleId;
        public boolean isChoiceRule;

        public RuleMeta(Integer ruleId, boolean isChoiceRule) {
            this.ruleId = ruleId;
            this.isChoiceRule = isChoiceRule;
        }
    }
}
