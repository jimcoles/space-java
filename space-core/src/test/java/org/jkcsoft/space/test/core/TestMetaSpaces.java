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

import org.junit.Test;

/**
 * Tests to ensure that loaded programs, as meta-level Spaces, are queryable just like
 * User-level Spaces.  In the RDB analogy, these tests are just queries
 * against the "System Tables".
 */
public class TestMetaSpaces {

    @Test
    public void testMetaSpaceQuery() {
        /*
        1. Load a known Space program file.
        2. QueryImpl the language Spaces for the expected objects.
         */
    }
}
