/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.antlr.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.FileUtils;
import org.jkcsoft.space.antlr.SpaceLexer;
import org.jkcsoft.space.antlr.SpaceParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class TestParser {

    @Test
    public void testParser() {
        try {
            System.out.println(FileUtils.getFile(".").getAbsolutePath());
            ANTLRInputStream input =
                    new ANTLRInputStream(
                            new FileInputStream(
                                    FileUtils.getFile("..", "space-core", "src", "test", "space", "hello",
                                                      "Hello.space")));

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
                    parseErrors.add("ambiguity");

                }

                @Override
                public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int
                        prediction, ATNConfigSet configs) {
                    parseErrors.add("ambiguity");

                }
            };

            SpaceLexer lexer = new SpaceLexer(input); // create a buffer of tokens pulled from the lexer
            lexer.addErrorListener(errorListener);
            CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
            SpaceParser parser = new SpaceParser(tokens);
            parser.addErrorListener(errorListener);
            ParseTree tree = parser.space(); // begin parsing at init rule
            Assert.assertTrue("parse errors", parseErrors.size() == 0);
            System.out.println(tree.toStringTree(parser)); // print LISP-style tree
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
