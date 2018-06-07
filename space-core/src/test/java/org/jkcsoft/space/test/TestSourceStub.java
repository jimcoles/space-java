package org.jkcsoft.space.test;
/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016 through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jkcsoft.space.lang.runtime.Executor;

import java.io.File;

public class TestSourceStub {

    private static final Logger log = Logger.getRootLogger();

    private Executor getExecutor() {
        Executor exec = null;
        try {
            exec = new Executor(null);
        } catch (Throwable th) {
            log.error("error running", th);
        }
        return exec;
    }

    public void runTestSource(String spaceSourcePath) {
        getExecutor().loadDir(FileUtils.getFile(getRootSrcDir(), spaceSourcePath));
    }

    private File getRootSrcDir() {
        return FileUtils.getFile("src", "test", "space");
    }

}
