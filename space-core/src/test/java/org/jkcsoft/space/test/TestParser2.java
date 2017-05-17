/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.test;

import org.apache.commons.io.FileUtils;
import org.jkcsoft.space.lang.runtime.loaders.antlr.g2.G2AntlrParser;
import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestParser2 {

    @Test
    public void testParser() {
        G2AntlrParser spaceParser = new G2AntlrParser();
        try {
            spaceParser.load(FileUtils.getFile("..", "space-core", "src", "test", "space2", "Hello.space"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}