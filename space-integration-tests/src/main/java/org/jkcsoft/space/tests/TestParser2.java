/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.tests;

import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestParser2 extends TestSourceStub {

    @Test
    public void testParser() {
        try {
            this.runTestSource("Hello.space");
        }
        catch (Exception e) {
            log("failed", e);
            throw e;
        }
    }
}
