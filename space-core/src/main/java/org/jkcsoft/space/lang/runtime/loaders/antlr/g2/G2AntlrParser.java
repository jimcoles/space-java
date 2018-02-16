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

import org.antlr.v4.Tool;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.ast.*;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.SpaceLexer;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.loader.AstLoader;
import org.jkcsoft.space.lang.runtime.loaders.antlr.AntlrTreePrintListener;
import org.jkcsoft.space.lang.runtime.loaders.antlr.AntlrUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.Comparator;
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

    private static final Logger log = Logger.getLogger(G2AntlrParser.class);
    private GrammarRootAST spaceGrammarRoot;

    @Override
    public AstFactory load(File file) throws Exception {
        log.info("Parsing file [" + file.getAbsolutePath() + "]");
        InputStream fileInputStream = new FileInputStream(file);
        ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
        return load(input);
    }

    private AstFactory load(ANTLRInputStream input) {
        AstFactory astFactory;List<String> parseErrors = new LinkedList<>();
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

        // Loads the ANTLR grammar for future use
        loadSpaceGrammar();

        if (spaceGrammarRoot != null) {
            log.debug("The grammar AST: " + spaceGrammarRoot.getGrammarName());
            List<GrammarAST> allRules = spaceGrammarRoot.getNodesWithType(ANTLRParser.RULE);

            // ANTLRParser.RULE; // first child is GrammarAST of RULE_REF getText() == Rule name
            // ANTLRParser.RULE_REF;
            // ANTLRParser.BLOCK; // when rule is a block of alternatives
            // ANTLRParser.ALT;   // Alternative? as with choice '|'

            //
            allRules.sort(Comparator.comparing(AntlrUtil::extractRuleName));
            for (GrammarAST rule : allRules) {
                String ruleName = AntlrUtil.extractRuleName(rule);
                String astClassName = Antrl2AstMapping.antlrRuleToAstClassname(ruleName);
                log.debug(
                        String.format(
                                "Rule Name: %25s can load %25s => %5b",
                                ruleName, astClassName,
                                Antrl2AstMapping.canLoad(Antrl2AstMapping.computeFQAstClassName(astClassName))
                        )
                );
            }
        }

        //
        SimpleTransListener stl = new SimpleTransListener(SpaceParser.ruleNames);
        log.info("Trans listener dump: " + JavaHelper.EOL
                + String.format("%3s %-25s %-25s %6s %-10s %5s", "Id", "Rule", "Wrapper", "Count", "Loadable", "Need")
                + JavaHelper.EOL
                + stl.dumpRuleStats());
        SpaceLexer spaceLexer = new SpaceLexer(input); // create a buffer of tokens pulled from the lexer
        spaceLexer.addErrorListener(errorListener);
        CommonTokenStream tokenStream = new CommonTokenStream(spaceLexer); // create a parser that feeds off the
        // tokens buffer
        SpaceParser spaceParser = new SpaceParser(tokenStream);
        //
        spaceParser.addErrorListener(errorListener);
        // dump using our customer printer ...
        AntlrTreePrintListener printListener = new AntlrTreePrintListener(spaceParser.getRuleNames());
        spaceParser.addParseListener(printListener);
        // begin parsing at top-level rule
        SpaceParser.ParseUnitContext parseUnitContext = spaceParser.parseUnit();
        log.info("Parse errors from ANTLR: " + Strings.buildCommaDelList(parseErrors));

        // debug / print
        log.info("ANTLR Util parse dump:" + JavaHelper.EOL
                + parseUnitContext.toStringTree(spaceParser));
        log.info("ANTLR custom print/dump: " + JavaHelper.EOL
                + printListener.getSb());

//        SimpleVisitor astTransVisitor = new SimpleVisitor();
        // accept() just calls back into the Visitor's visitParseUnit() method
        // with a nicely loaded context object
//        log.info("attempt visitor pattern for AST transform");
//        AstFactory accept = parseUnitContext.accept(astTransVisitor);

        Ra2AstTransform ra2AstTransform = new Ra2AstTransform();
        astFactory = ra2AstTransform.transform(parseUnitContext);

        return astFactory;
    }

    private void loadSpaceGrammar() {
        // If needed, a way to interogate the Space grammar.
        Tool antlrGrammar = new Tool();
        spaceGrammarRoot = null;
//        String fileName = "./src/main/antlr4/org/jkcsoft/space/antlr/SpaceParser.g4";
        String grammarFileName = "/org/jkcsoft/space/antlr/SpaceParser.g4";
        try {
//            File file = new File(grammarFileName);
            InputStream inputStream = this.getClass().getResourceAsStream(grammarFileName);
//            org.antlr.runtime.ANTLRFileStream in = new org.antlr.runtime.ANTLRFileStream(file.getAbsolutePath());
            org.antlr.runtime.ANTLRInputStream antlrInputStream = new org.antlr.runtime.ANTLRInputStream(inputStream);
            spaceGrammarRoot = antlrGrammar.parse(grammarFileName, antlrInputStream);
        }
        catch (IOException ioe) {
            log.error("Could not find Space grammar file ["+grammarFileName+"]", ioe);
        }
    }

}
