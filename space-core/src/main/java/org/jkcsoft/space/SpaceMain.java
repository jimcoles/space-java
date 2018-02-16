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
import org.apache.log4j.Logger;
import org.jkcsoft.space.lang.runtime.Executor;

import java.util.List;

/**
 * @author Jim Coles
 */
public class SpaceMain {

    private static final Logger log = Logger.getLogger(SpaceMain.class);

    public static void main(String[] args) {
        try {
            instMain(args);
        } catch (Exception e) {
            log.fatal("error running Space", e);
        }
        return;
    }

    private static void instMain(String[] args) throws ParseException {
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

        CommandLineParser cliParse = new DefaultParser();
        CommandLine commandLine = cliParse.parse(cliOpts, args);

        String fileName = commandLine.getOptionValue(optFileName.getOpt());

        List<String> restOfArgs = commandLine.getArgList();

        HelpFormatter helper = new HelpFormatter();
        helper.printHelp("syntax", "header", cliOpts, "footer", true);

        // Exec specified Space code ...
        Executor exec = new Executor();
        exec.run(fileName);
    }

}
