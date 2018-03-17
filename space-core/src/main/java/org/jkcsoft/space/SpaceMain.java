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
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.io.PrintStream;
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

    private SpaceMain() {
        app = new Application("space");
    }

    private void instMain(String[] args) {
        try {
            Options cliOpts = new Options();
            OptionGroup optionGroup = new OptionGroup();

            Option optFileName = new Option("file", true, "File name to run");
            Option help = new Option("help", "Print usage");
            Option version = new Option("version", "Show Space version info");
            Option verbose = new Option("verbose", "Print extra runtime info to standard out");

            cliOpts.addOption(optFileName);
            cliOpts.addOption(help);
            cliOpts.addOption(version);
            cliOpts.addOption(verbose);

            // Parse and validate the command line based on options decl ...
            CommandLineParser cliParse = new DefaultParser();
            CommandLine commandLine = cliParse.parse(cliOpts, args);
            validate(commandLine);

            // Interpret and apply the command line options ...

            // Opt: help
            if (commandLine.hasOption(help.getOpt())) {
                printUsage(cliOpts);
                return;
            }

            // Opt: verbose => set log level to DEBUG
            if (commandLine.hasOption(verbose.getOpt())) {
                Logger.getRootLogger().setLevel(Level.DEBUG);
            }

            // Opt: file - Source file to run
            String fileName = commandLine.getOptionValue(optFileName.getOpt());

            if (Strings.isEmpty(fileName)) {
                List<String> restOfArgs = commandLine.getArgList();
                if (restOfArgs == null || restOfArgs.size() != 1)
                    throw new IllegalArgumentException("Filename required");

                fileName = restOfArgs.get(0);
            }

            // Exec specified Space code ...
            Executor exec = new Executor();
            exec.run(fileName);
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

}
