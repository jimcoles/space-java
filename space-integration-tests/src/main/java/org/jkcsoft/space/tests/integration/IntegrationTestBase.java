/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.tests.integration;

import org.apache.commons.io.FileUtils;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.RunResults;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class IntegrationTestBase {

    private static final Logger log = LoggerFactory.getLogger("ROOT");

    @BeforeClass
    public static void prepTest() {

    }

    private Executor getDefaultExecutor(String spaceSourcePath) {
        Executor exec = null;
        try {
            Executor.ExeSettings exeSettings = new CallableExeSettings(spaceSourcePath,
                                                                       List.of(getSpaceLangSrcDir()));
            exec = Executor.getInstance(exeSettings);
        } catch (Throwable th) {
            log.error("error running", th);
        }
        return exec;
    }

    public File getSpaceLangSrcDir() {
        return FileUtils.getFile("space-lang-lib", "src", "main", "space");
    }

    public void runTestSource(String spaceSourcePath) {
        runTestSource(spaceSourcePath, 0, 0);
    }

    public void runTestSource(String spaceSourcePath, int expectedErr, int expectedWarn) {
        log("executing source file [{}]", spaceSourcePath);
        Executor exec = getDefaultExecutor(spaceSourcePath);
//        String runSrcDir = FileUtils.getFile(spaceSourcePath).getParent();
//        exec.loadSrcRootDir(FileUtils.getFile(getRootSrcDir(), runSrcDir));
        RunResults runResults = exec.run();
        int[] errLevelCounts = runResults.getLevelCounts();
        Assert.assertEquals(errLevelCounts[AstLoadError.Level.ERROR.ordinal()], expectedErr);
        Assert.assertEquals(errLevelCounts[AstLoadError.Level.WARN.ordinal()], expectedWarn);
    }

    public File getRootSrcDir() {
        return FileUtils.getFile("src", "test", "space");
    }

    public void log(String msg, Object ... args) {
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
