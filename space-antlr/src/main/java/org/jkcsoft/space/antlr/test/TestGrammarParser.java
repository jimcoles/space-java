/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.antlr.test;

import org.jkcsoft.space.lang.ast.Schema;
import org.jkcsoft.space.lang.loader.AstLoader;

import java.io.File;

public class TestGrammarParser implements AstLoader {

    @Override
    public Schema load(File file) throws Exception {
        return null;
    }

    @Override
    public String getName() {
        return "Dummy Loader";
    }
}
