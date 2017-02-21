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

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.Space2Lexer;
import org.jkcsoft.space.antlr.Space2Parser;
import org.jkcsoft.space.lang.ast.AstBuilder;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.runtime.loaders.antlr.AntlrTreeNodePrinter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
public class G2AntlrParser implements AstLoader {

    private static final Logger log = Logger.getRootLogger();

    @Override
    public AstBuilder load(File file) throws Exception {
        log.info("Parsing file: " + file.getAbsolutePath());
        InputStream fileInputStream = new FileInputStream(file);
        ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
        return load(input);
    }

    private AstBuilder load(ANTLRInputStream input) {
        AstBuilder astBuilder;List<String> parseErrors = new LinkedList<>();
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
        Space2Parser.ParseUnitContext parseUnitContext = spaceParser.parseUnit();
        log.info("Parse errors from ANTLR: " + Strings.buildCommaDelList(parseErrors));

        // debug / print
        log.info("ANTLR Util parse dump:" + "\n" + parseUnitContext.toStringTree(spaceParser));
        log.info("ANTLR custom print/dump: " + "\n" + printer.getSb());

//            Ra2AstTransformVisitor astTransVisitor = new Ra2AstTransformVisitor();
        // accept() just calls back into the Visitor's visitParseUnit() method
        // with a nicely loaded context object
//            Void accept = parseUnitContext.accept(astTransVisitor);

        Ra2AstTransform ra2AstTransform = new Ra2AstTransform();
        astBuilder = ra2AstTransform.toAst(parseUnitContext);

        return astBuilder;
    }

}
