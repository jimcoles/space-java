/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.antlr.loaders;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.FileSourceInfo;
import org.jkcsoft.space.lang.ast.SourceInfo;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Holds logic that encapsulates out desired mapping from ANTLR grammar to the Space
 * AST.  E.g., name mangling, mapping from ANTRL composition to Java sub-classing.
 *
 * @author Jim Coles
 */
public class Antrl2AstMapping {

    public static final String AST_PACKAGE_NAME = AstFactory.class.getPackage().getName();

    private static SortedMap<Integer, RuleMeta> ruleMetas = new TreeMap<>();

    static {
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_statement);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_valueOrAssignmentExpr);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_expression);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_anyThing);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_parameterDecl);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_valueExprChain);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_valueOrAssignmentExpr);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_comment);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_anyTypeRef);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_primitiveTypeName);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_literalExpr);
        Antrl2AstMapping.addRuleMeta(SpaceParser.RULE_scalarLiteral);
    }

    public static String antlrRuleToAstClassname(String ruleName) {
        return Character.toUpperCase(ruleName.charAt(0))
                + ruleName.substring(1, ruleName.length());
    }

    public static String computeFQAstClassName(String astClassName) {
        return AST_PACKAGE_NAME + "." + astClassName;
    }

    static RuleMeta addRuleMeta(int ruleId) {
        return ruleMetas.put(ruleId, new RuleMeta(ruleId, true));
    }

    public static boolean canLoad(String astClassFQN) {
        boolean canLoad = false;
        try {
            Class<?> aClass = Class.forName(astClassFQN);
//            aClass.getConstructor();
            canLoad = true;
        } catch (ClassNotFoundException e) {
            canLoad = false;
        }
        return canLoad;
    }

    public static boolean shouldCoalesce(int ruleId) {
        Antrl2AstMapping.RuleMeta ruleMeta = ruleMetas.get(ruleId);
        return ruleMeta != null && ruleMeta.isChoiceRule;
    }

    public static RuleMeta getRuleMapping(Integer key) {
        return ruleMetas.get(key);
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

    public static SourceInfo toAst(File file, ParserRuleContext parserRuleContext) {
        return new FileSourceInfo(
                file,
                toAst(parserRuleContext.start, false),
                toAst(parserRuleContext.stop, true));
    }

    public static SourceInfo.FileCoord toAst(Token tokenAO, boolean end) {
        return new SourceInfo.FileCoord() {
            @Override
            public boolean isStart() {
                return !end;
            }

            @Override
            public int getLine() {
                return tokenAO == null ? -1 : tokenAO.getLine();
            }

            @Override
            public int getCursorIndexInLine() {
                return (
                    tokenAO == null ? -1 :
                        isStart() ?
                            tokenAO.getCharPositionInLine()
                            : (tokenAO.getCharPositionInLine() + 1) + (tokenAO.getStopIndex() - tokenAO.getStartIndex())
                );
            }

            @Override
            public String toString() {
                return getLine() + "," + (isStart() ? getCursorIndexInLine() + 1 : getCursorIndexInLine());
            }
        };
    }
}
