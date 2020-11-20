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
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.RunResults;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class IntegrationTestBase {

    private static final Logger log = LoggerFactory.getLogger("ROOT");

    @BeforeClass
    public static void prepTest() {

    }

    private Executor getDefaultExecutor(String spaceMainType, String ... testSpecificDirPaths) {
        Executor exec = null;
        try {
            List<File> testSpecificDirs = new LinkedList<>();
            for (String testSpecificDirPath : testSpecificDirPaths) {
                testSpecificDirs.add(FileUtils.getFile(testSpecificDirPath));
            }
            testSpecificDirs.addAll(List.of(getSpaceLangSrcDir()));
            Executor.ExeSettings exeSettings = new CallableExeSettings(spaceMainType,testSpecificDirs);
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

    public void runTestSource(String spaceMainType, int expectedErr, int expectedWarn, String ... testDirPaths) {
        log("executing source file [{}]", spaceMainType);
        Executor exec = getDefaultExecutor(spaceMainType, testDirPaths);
//        String runSrcDir = FileUtils.getFile(spaceSourcePath).getParent();
//        exec.loadSrcRootDir(FileUtils.getFile(getRootSrcDir(), runSrcDir));
        RunResults runResults = exec.run();
        int[] errLevelCounts = runResults.getLevelCounts();
        boolean fail = false;
        //
        if (!assertLevel(expectedWarn, errLevelCounts, AstLoadError.Level.WARN)
            || !assertLevel(expectedErr, errLevelCounts, AstLoadError.Level.ERROR))
        {
            String coreErrMsgTmp = "errors ({0}) warnings ({1})";
            String errMsg =
                "exceeded space compiler load-link problems" + JavaHelper.EOL
                    + "Expected: " + Strings.replace(coreErrMsgTmp, expectedErr, expectedWarn) + JavaHelper.EOL
                    + "Actual: " + Strings.replace(coreErrMsgTmp, errLevelCounts[AstLoadError.Level.ERROR.ordinal()],
                                                   errLevelCounts[AstLoadError.Level.WARN.ordinal()]);
            Assert.fail(errMsg);
        }
        //
        Assert.assertNull("Space runtime error trapped", runResults.getRuntimeError());
    }

    private boolean assertLevel(int expectedCount, int[] errLevelCounts, AstLoadError.Level level) {
        return expectedCount == -1 || errLevelCounts[level.ordinal()] <= expectedCount;
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
