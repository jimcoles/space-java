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

import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestSourceLLE extends IntegrationTestBase {

    @Test
    public void testHello() {
        runTestSource("Hello", 0, -1, "./space-integration-tests/src/test/space/hello");
    }

    @Test
    public void testNtmu() {
        runTestSource("Ntmu", 0, -1, "./space-integration-tests/src/test/space/ntmu");
    }

    @Test
    public void testSimpleCore() {
        runTestSource("TestSimpleCore", 0, -1, "./space-integration-tests/src/test/space/simplecore");
    }

}
