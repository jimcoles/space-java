/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.test.core;

import org.apache.commons.io.FileUtils;
import org.jkcsoft.space.lang.runtime.Executor;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class TestSourceStub {

    private static final Logger log = LoggerFactory.getLogger("ROOT");

    private Executor exe;

    protected Executor getNewApiExec() {
        initApiExe();
        return exe;
    }

    void initApiExe() {
        exe = Executor.getInstance(new CallableExeSettings(null, List.of(getSpaceLangSrcDir())));
        log.info("instantiated Space exe");
    }

    public File getSpaceLangSrcDir() {
        return FileUtils.getFile("space-lang-lib", "src", "main", "space");
    }

    public File getRootSrcDir() {
        return FileUtils.getFile("src", "test", "space");
    }

    public void log(String msg, Object... args) {
        log.info(msg, args);
    }

    private static class CallableExeSettings implements Executor.ExeSettings {
        private String exeMain;
        private List<File> spaceDirs;

        public CallableExeSettings(String exeMain, List<File> spaceDirs) {
            this.exeMain = exeMain;
            this.spaceDirs = spaceDirs;
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
