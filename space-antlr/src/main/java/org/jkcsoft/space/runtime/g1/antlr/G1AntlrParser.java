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
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.SpaceLexer;
import org.jkcsoft.space.antlr.SpaceParser;

import java.io.File;
import java.io.FileInputStream;
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

            print(parseErrors);

            SpaceLexer spaceLexer = new SpaceLexer(input); // create a buffer of tokens pulled from the lexer
            spaceLexer.addErrorListener(errorListener);
            CommonTokenStream tokens = new CommonTokenStream(spaceLexer); // create a parser that feeds off the
            // tokens buffer
            SpaceParser spaceParser = new SpaceParser(tokens);
            spaceParser.addErrorListener(errorListener);
            ParseTree tree = spaceParser.list(); // begin parsing at init rule

            // debug / print
            String stringTree = tree.toStringTree(spaceParser);
            System.out.println("ANRLR Util parse dump:" + "\n" + stringTree); // print LISP-style tree

            //
            String[] ruleNames = spaceParser.getRuleNames();
            ATN atn = spaceParser.getATN();
            Token currentToken = spaceParser.getCurrentToken();
            ParserRuleContext context = spaceParser.getContext();
            List<String> dfaStrings = spaceParser.getDFAStrings();

            // walk the raw parse tree to build our AST ...
            RaTreeWalker walker = new RaTreeWalker();
            RaTreePrinter printer = new RaTreePrinter();
            walker.addListener(printer);
            walker.visitAll(tree, spaceParser);
            System.out.println("Formatted ANTLR parse dump:" + "\n" + printer.getSb());
            //
            walker = new RaTreeWalker();
            Ra2ImTransform ra2ImTransform = new Ra2ImTransform();
            walker.addListener(ra2ImTransform);
            walker.visitAll(tree, spaceParser);
            //
            ImTreePrinter imPrinter = new ImTreePrinter();
            ImTreeWalker imWalker = new ImTreeWalker();
            imWalker.addListener(imPrinter);
            imWalker.visitAll(ra2ImTransform.getRootNode());
            System.out.println("Formatted IM parse dump:" + "\n" + imPrinter.getSb());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(List<String> parseErrors) {
        System.err.append("Parse Errors: " + Strings.buildCommaDelList(parseErrors));
    }

}
