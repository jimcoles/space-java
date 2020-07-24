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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jim Coles
 */
public class TestSplit {

    private static final Logger log = LoggerFactory.getLogger("ROOT");

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
