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
import org.apache.log4j.Logger;
import org.jkcsoft.apps.Application;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.runtime.Executor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class SpaceMain {

    private static final Logger log = Logger.getLogger(SpaceMain.class);

    public static void main(String[] args) {
        try {
            SpaceMain main = new SpaceMain();
            main.instMain(args);
        } catch (Exception e) {
            log.fatal("last ditch error handler =>", e);
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

            // Opt: verbose => set log level to DEBUG
            if (commandLine.hasOption(optVerbose.getOpt())) {
                Logger.getRootLogger().setLevel(Level.DEBUG);
            }

            // Exec specified Space code ...
            Executor exec = new Executor(new CliExeSettings(commandLine));
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

    private class CliExeSettings implements Executor.ExeSettings {

        private String exeMain;
        private List<File> spaceDirs = new LinkedList<>();

        public CliExeSettings(CommandLine commandLine) {
            // Opt: verbose => set log level to DEBUG
            if (commandLine.hasOption(optVerbose.getOpt())) {
                Logger.getRootLogger().setLevel(Level.DEBUG);
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
