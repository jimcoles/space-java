/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.runtime.g1.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.SpaceLexer;
import org.jkcsoft.space.antlr.SpaceParser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * This class wraps the Antlr Scanner (Tokenizer, Lexer) and Parser (Recognizer).
 * <p>
 * Accepts tokens and builds the first parse tree.
 *
 * @author Jim Coles
 */
public class G1AntlrParser {

//    private static Logger print = Logger.getRootLogger();

    public void parse(File file) {
        try {
            System.out.println("Parsing file: " + file.getAbsolutePath());
            ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(file));

            List<String> parseErrors = new LinkedList<>();
            ANTLRErrorListener errorListener = new ANTLRErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int
                        charPositionInLine, String msg, RecognitionException e) {
                    parseErrors.add(msg);
                }

                @Override
                public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
                                            BitSet ambigAlts, ATNConfigSet configs) {
                    parseErrors.add("ambiguity");
                }

                @Override
                public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                                                        BitSet conflictingAlts, ATNConfigSet configs) {
                    parseErrors.add("attempting full context");
                }

                @Override
                public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int
                        prediction, ATNConfigSet configs) {
                    parseErrors.add("context sensitivity");
                }
            };

            print(parseErrors);

            SpaceLexer spaceLexer = new SpaceLexer(input); // create a buffer of tokens pulled from the lexer
            spaceLexer.addErrorListener(errorListener);
            CommonTokenStream tokens = new CommonTokenStream(spaceLexer); // create a parser that feeds off the
            // tokens buffer
            SpaceParser spaceParser = new SpaceParser(tokens);
            spaceParser.addErrorListener(errorListener);
            ParseTree tree = spaceParser.space(); // begin parsing at init rule

            // debug / print
            String stringTree = tree.toStringTree(spaceParser);
            System.out.println("Antlr Util parse dump:" + "\n" + stringTree); // print LISP-style tree

            //
            String[] ruleNames = spaceParser.getRuleNames();
            ATN atn = spaceParser.getATN();
            Token currentToken = spaceParser.getCurrentToken();
            ParserRuleContext context = spaceParser.getContext();
            List<String> dfaStrings = spaceParser.getDFAStrings();

            // walk the raw parse tree to build our AST ...
            StringBuilder sbDump = new StringBuilder();
            buildAst(tree, spaceParser, sbDump);
            System.out.println("Raw parse dump:" + "\n" + sbDump);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void print(List<String> parseErrors) {
        System.err.append("Parse Errors: " + Strings.buildCommaDelList(parseErrors));
    }

}
