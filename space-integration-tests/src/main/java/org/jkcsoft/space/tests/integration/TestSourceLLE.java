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
        runTestSource("Hello.space");
    }

    @Test
    public void testNtmu() {
        runTestSource("Ntmu.space");
    }

    @Test
    public void testSimpleCore() {
        runTestSource("TestSimpleCore.space");
    }

}
