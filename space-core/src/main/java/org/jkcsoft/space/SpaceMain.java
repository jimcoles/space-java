/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space;

import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.jkcsoft.apps.Application;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.runtime.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class SpaceMain implements ScriptEngine {

    private static final Logger log = LoggerFactory.getLogger(SpaceMain.class);

    public static void main(String[] args) {
        try {
            SpaceMain main = new SpaceMain();
            main.instMain(args);
        } catch (Exception e) {
            log.error("last ditch error handler =>", e);
        }
        return;
    }

    // -------------------------------------------------------------------------
    //
    private Application app;
    private Option optMainName = new Option("main", true, "Main to run, dot-separated");
    private Option optDirLibs = new Option("dirs", true, "Colon-separated sequence of (Space) directory roots");
    private Option optVersion = new Option("version", "Show Space version info");
    private Option optVerbose = new Option("verbose", "Print extra runtime info to standard out");
    private Option optHelp = new Option("help", "Print usage");

    private SpaceMain() {
        app = new Application("space");
    }

    private void instMain(String[] args) {
        try {
            Options cliOpts = new Options();
            OptionGroup optionGroup = new OptionGroup();

            cliOpts.addOption(optMainName);
            cliOpts.addOption(optDirLibs);
            cliOpts.addOption(optHelp);
            cliOpts.addOption(optVersion);
            cliOpts.addOption(optVerbose);

            // Parse and validate the command line based on options decl ...
            CommandLineParser cliParse = new DefaultParser();
            CommandLine commandLine = cliParse.parse(cliOpts, args);
            validate(commandLine);

            // Interpret and apply the command line options ...

            // Opt: help
            if (commandLine.hasOption(optHelp.getOpt())) {
                printUsage(cliOpts);
                return;
            }

            // Exec specified Space code ...
            Executor exec = Executor.getInstance(new CliExeSettings(commandLine));
            exec.run();
        }
        catch (ParseException e) {
            String message = "invalid command line: " + e.getMessage();
            log.error(message);
            System.err.println(message);
        }
    }

    private void printUsage(Options cliOpts) {
        HelpFormatter helper = new HelpFormatter();
        helper.printHelp(app.getName(), "", cliOpts, "", true);
    }

    private static void validate(CommandLine commandLine) {

    }

    //
    // ===================== ScriptEngine methods ==============================
    // NOTE: I'm not sure that a JSR 223 scripting engine is what we want.
    //
    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        return null;
    }

    @Override
    public void put(String key, Object value) {

    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public Bindings getBindings(int scope) {
        return null;
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {

    }

    @Override
    public Bindings createBindings() {
        return null;
    }

    @Override
    public ScriptContext getContext() {
        return null;
    }

    @Override
    public void setContext(ScriptContext context) {

    }

    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }

    private class CliExeSettings implements Executor.ExeSettings {

        private String exeMain;
        private List<File> spaceDirs = new LinkedList<>();

        public CliExeSettings(CommandLine commandLine) {
            // Opt: verbose => set log level to DEBUG
            // Opt: verbose => set log level to DEBUG
            if (commandLine.hasOption(optVerbose.getOpt())) {
                org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
            }

            // Opt: file - Source file to run
            exeMain = commandLine.getOptionValue(optMainName.getOpt());

            if (Strings.isEmpty(exeMain)) {
                List<String> restOfArgs = commandLine.getArgList();
                if (restOfArgs == null || restOfArgs.size() != 1)
                    throw new IllegalArgumentException("Filename required");

                exeMain = restOfArgs.get(0);
            }

            String dirSeqExpr = commandLine.getOptionValue(optDirLibs.getOpt());
            String[] dirNames = dirSeqExpr.split(":");
            for (String dirName : dirNames) {
                spaceDirs.add(new File(dirName));
            }
        }

        @Override
        public String getExeMain() {
            return exeMain;
        }

        @Override
        public List<File> getSpaceDirs() {
            return spaceDirs;
        }
    }
}
