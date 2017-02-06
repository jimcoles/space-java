/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.antlr.test;

import org.apache.commons.io.FileUtils;
import org.jkcsoft.space.runtime.g2.antlr.G2AntlrParser;
import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestParser2 {

    @Test
    public void testParser() {
        G2AntlrParser spaceParser = new G2AntlrParser();
        spaceParser.parse(FileUtils.getFile("..", "space-core", "src", "test", "space2", "Hello.space"));
    }
}
