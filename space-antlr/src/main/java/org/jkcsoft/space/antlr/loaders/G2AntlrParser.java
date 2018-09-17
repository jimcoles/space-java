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

import org.antlr.v4.Tool;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.tool.ast.*;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.antlr.SpaceLexer;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.*;
import org.jkcsoft.space.antlr.AntlrTreePrintListener;
import org.jkcsoft.space.antlr.AntlrUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This class wraps the ANTLR Scanner (Tokenizer, Lexer) and Parser (Recognizer).
 * <p>
 * Accepts tokens and builds the first parse tree.
 *
 * @author Jim Coles
 */
public class G2AntlrParser implements AstLoader {

    private static final Logger log = Logger.getLogger(G2AntlrParser.class);

    // The Space grammar itself is, of course, really a static thing ...
    private GrammarRootAST spaceGrammarRoot;

    private File srcFile;
    private AstFactory astFactory = AstFactory.getInstance();

    public G2AntlrParser() {
        // Loads the ANTLR grammar for future use
        loadSpaceGrammar();

        // Compares Space grammar to the Java AST classes and reports on sufficiency of the
        // latter.
        checkAstClasses();
    }

    @Override
    public String getName() {
        return "ANTLR";
    }

    @Override
    public DirLoadResults loadDir(File srcRootDir) throws IOException {
        log.info("Loading source in dir [" + srcRootDir.getAbsolutePath() + "]");
        DirLoadResults results = new DirLoadResults(srcRootDir);
        Namespace tempNs = astFactory.newNamespace(null, "temp");
        Directory spcRootDir = tempNs.getRootDir();
        loadChildren(results, spcRootDir, srcRootDir);
        results.setSpaceRootDir(spcRootDir);
        return results;
    }

    @Override
    public FileLoadResults loadFile(Directory spaceDir, File spaceSrcFile) throws IOException {
        log.info("Parsing file [" + spaceSrcFile.getAbsolutePath() + "]");
        this.srcFile = spaceSrcFile;
        AstFileLoadErrorSet fileErrors = new AstFileLoadErrorSet(spaceSrcFile);
        FileLoadResults results = new FileLoadResults();
        ParseUnit parseUnit = loadInputStream(fileErrors, new ANTLRInputStream(new FileInputStream(spaceSrcFile)));
        //
        if (parseUnit != null) {
            spaceDir.addParseUnit(parseUnit);
        }
        //
        results.setParseUnit(parseUnit);
        if (fileErrors.hasErrors())
            results.getErrors().addAll(fileErrors.getAllErrors());
        //
        return results;
    }

    @Override
    public Directory loadFromResource(String path) {
        return null;
    }

    private void loadChildren(DirLoadResults dirResults, Directory spcContainerDir, File srcParentDir) throws IOException {
        for (File childFile : srcParentDir.listFiles()) {
            if (childFile.isDirectory()) {
                Directory spcChildDir = astFactory.newAstDir(null, childFile.getName());
                // create new space container corresponding to arg src dir
                spcContainerDir.addDir(spcChildDir);
                // recurse to add children of arg src dir to new space container
                loadChildren(dirResults, spcChildDir, childFile);
            }
            else {
                if (childFile.getName().endsWith(Language.SPACE.getFileExt())) {
                    FileLoadResults fileResults = loadFile(spcContainerDir, childFile);
                    if (fileResults.getParseUnit() != null)
                        spcContainerDir.addParseUnit(fileResults.getParseUnit());
                    dirResults.getErrorList().addAll(fileResults.getErrors());
                }
            }
        }
    }

    public ParseUnit parseExpr(AstFileLoadErrorSet errors, String spaceExpr) {
        log.info("Parsing expression [" + spaceExpr.substring(0, 25) + "...]");
        ParseUnit parseUnit = loadInputStream(errors, new ANTLRInputStream(spaceExpr));
        return parseUnit;
    }

    private ParseUnit loadInputStream(AstFileLoadErrorSet loadErrors, ANTLRInputStream aisSpaceSrc) {
        ANTLRErrorListener errorListener = new MyANTLRErrorListener(loadErrors);
        //
        SimpleTransListener stl = new SimpleTransListener(SpaceParser.ruleNames);
        log.debug("Trans listener dump: (commented out in code)");
//        log.debug("Trans listener dump: " + JavaHelper.EOL
//                      + String
//            .format("%3s %-25s %-25s %6s %-10s %5s", "Id", "Rule", "Wrapper", "Count", "Loadable", "Need")
//                      + JavaHelper.EOL
//                      + stl.dumpRuleStats());

        SpaceLexer srcLexerStream = new SpaceLexer(aisSpaceSrc); // create a buffer of tokens pulled from the lexer
        srcLexerStream.addErrorListener(errorListener);
        CommonTokenStream srcTokenStream = new CommonTokenStream(srcLexerStream); // create a parser that feeds off the
        // tokens buffer
        SpaceParser srcParser = new SpaceParser(srcTokenStream);
        //
        srcParser.addErrorListener(errorListener);
        // dump using our customer printer ...
        AntlrTreePrintListener printListener = new AntlrTreePrintListener(srcParser.getRuleNames());
        srcParser.addParseListener(printListener);
        // begin parsing at top-level rule
        SpaceParser.ParseUnitContext parseUnitContext = srcParser.parseUnit();
        log.info("Parse errors from ANTLR: " + Strings.buildNewlineList(loadErrors.getAllErrors()));

        // debug / print
        log.trace("ANTLR Util parse dump:" + JavaHelper.EOL
                      + parseUnitContext.toStringTree(srcParser));
        log.trace("ANTLR custom print/dump: " + JavaHelper.EOL
                      + printListener.getSb());

//        SimpleVisitor astTransVisitor = new SimpleVisitor();
        // accept() just calls back into the Visitor's visitParseUnit() method
        // with a nicely loaded context object
//        log.info("attempt visitor pattern for AST transform");
//        AstFactory accept = parseUnitContext.accept(astTransVisitor);

        ParseUnit parseUnit = null;

        if (!loadErrors.hasSyntaxErrors()) {
            Antlr2AstTransform antlr2AstTransform = new Antlr2AstTransform(astFactory, srcFile);
            parseUnit = antlr2AstTransform.transform(parseUnitContext);
        }

        return parseUnit;
    }

    private void checkAstClasses() {
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
    }

    private void loadSpaceGrammar() {
        // If needed, a way to interogate the Space grammar.
        Tool antlrGrammar = new Tool();
        spaceGrammarRoot = null;
        String grammarFileName = "/org/jkcsoft/space/antlr/SpaceParser.g4";
        try {
//            File file = new File(grammarFileName);
            InputStream inputStream = this.getClass().getResourceAsStream(grammarFileName);
//            org.antlr.runtime.ANTLRFileStream in = new org.antlr.runtime.ANTLRFileStream(file.getAbsolutePath());
            org.antlr.runtime.ANTLRInputStream antlrInputStream = new org.antlr.runtime.ANTLRInputStream(inputStream);
            spaceGrammarRoot = antlrGrammar.parse(grammarFileName, antlrInputStream);
        } catch (IOException ioe) {
            log.error("Could not find Space grammar file [" + grammarFileName + "]", ioe);
        }
    }

    private class MyANTLRErrorListener implements ANTLRErrorListener {
        private final AstFileLoadErrorSet parseErrors;

        public MyANTLRErrorListener(AstFileLoadErrorSet parseErrors) {
            this.parseErrors = parseErrors;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int
            charPositionInLine, String msg, RecognitionException e)
        {
            AntrlParseFileCoord start = new AntrlParseFileCoord(line, charPositionInLine, true);
            parseErrors.add(new AstLoadError(AstLoadError.Type.SYNTAX, new FileSourceInfo(srcFile, start, start), msg));
        }

        @Override
        public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
                                    BitSet ambigAlts, ATNConfigSet configs)
        {
            // TODO Not sure how to use provided params to inform user
            parseErrors.add(new AstLoadError(AstLoadError.Type.PARSE_WARNING, new FileSourceInfo(srcFile, null, null), "ambiguity"));
        }

        @Override
        public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                                                BitSet conflictingAlts, ATNConfigSet configs)
        {
            parseErrors.add(new AstLoadError(AstLoadError.Type.PARSE_WARNING, new FileSourceInfo(srcFile, null, null), "attempting full context"));
        }

        @Override
        public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int
            prediction, ATNConfigSet configs)
        {
            parseErrors.add(new AstLoadError(AstLoadError.Type.PARSE_WARNING, new FileSourceInfo(srcFile, null, null), "context sensitivity"));
        }

        private class AntrlParseFileCoord implements SourceInfo.FileCoord {
            private int line, cursorIndexInLine;
            private boolean isStart;
            public AntrlParseFileCoord(int line, int charPositionInLine, boolean isStart) {
                this.line = line;
                this.cursorIndexInLine = charPositionInLine;
                this.isStart = isStart;
            }

            @Override
            public boolean isStart() {
                return isStart;
            }

            @Override
            public int getLine() {
                return line;
            }

            @Override
            public int getCursorIndexInLine() {
                return cursorIndexInLine;
            }

            @Override
            public String toString() {
                return getLine() + "," + getCursorIndexInLine();
            }
        }
    }
}
