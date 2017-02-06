/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.runtime.g2.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.Space2Lexer;
import org.jkcsoft.space.antlr.Space2Parser;
import org.jkcsoft.space.runtime.AntlrTreeNodePrinter;

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
public class G2AntlrParser {

    private static Logger log = Logger.getRootLogger();

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

            Space2Lexer spaceLexer = new Space2Lexer(input); // create a buffer of tokens pulled from the lexer
            spaceLexer.addErrorListener(errorListener);
            CommonTokenStream tokenStream = new CommonTokenStream(spaceLexer); // create a parser that feeds off the
            // tokens buffer
            Space2Parser spaceParser = new Space2Parser(tokenStream);
            //
            spaceParser.addErrorListener(errorListener);
            // dump using our customer printer ...
            String[] ruleNames = spaceParser.getRuleNames();
            List<String> ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
            AntlrTreeNodePrinter printer = new AntlrTreeNodePrinter(ruleNamesList);
            spaceParser.addParseListener(printer);
            // begin parsing at top-level rule
            ParseTree tree = spaceParser.parseUnit();

            // debug / print
            log.info("ANTLR Util parse dump:" + "\n" + tree.toStringTree(spaceParser));
            log.info("ANTLR custom print/dump: " + "\n" + printer.getSb());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(List<String> parseErrors) {
        System.err.append("Parse Errors: " + Strings.buildCommaDelList(parseErrors));
    }

}
