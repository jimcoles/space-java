/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jim Coles
 */
public class TestSplit {

    private static final Logger log = Logger.getRootLogger();

    @Test
    public void testSplit() {
        split("java:java.lang.String", ":");
        split("java.lang.String", ":");
        split("java.lang.String", "\\.");
    }

    private void split(String inString, String regex) {
        String[] split = inString.split(regex);
        log.info("Split string [" + inString + "] w/ regex [" + regex + "] => " + Arrays.toString(split));
    }
}
