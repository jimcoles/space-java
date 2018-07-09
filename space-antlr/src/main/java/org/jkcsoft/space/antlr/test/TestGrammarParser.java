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

import org.jkcsoft.space.lang.ast.Directory;
import org.jkcsoft.space.lang.ast.ParsableChoice;
import org.jkcsoft.space.lang.ast.ParseUnit;
import org.jkcsoft.space.lang.loader.AstErrors;
import org.jkcsoft.space.lang.loader.AstLoader;

import java.io.File;
import java.io.IOException;

public class TestGrammarParser implements AstLoader {

    @Override
    public String getName() {
        return "Dummy Loader";
    }

    @Override
    public ParseUnit loadFile(AstErrors parentErrors, Directory spaceDir, File spaceSrcFile) throws IOException {
        return null;
    }

    @Override
    public Directory loadDir(AstErrors parentErrors, File srcDir) throws IOException {
        return null;
    }
}
