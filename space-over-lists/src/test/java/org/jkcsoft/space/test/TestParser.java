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
import org.jkcsoft.space.lang.runtime.loaders.antlr.g1.g1.G1AntlrParser;
import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestParser {

    @Test
    public void testParser() {
        G1AntlrParser spaceParser = new G1AntlrParser();
        spaceParser.parse(FileUtils.getFile("..", "space-core", "src", "test", "space", "hello",
                "HelloNorm.space"));
    }
}
