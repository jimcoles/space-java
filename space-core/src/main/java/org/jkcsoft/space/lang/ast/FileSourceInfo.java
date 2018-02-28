/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.io.File;

/**
 * @author Jim Coles
 */
public class FileSourceInfo implements SourceInfo {

    private File file;
    private int line;
    private int character;

    public FileSourceInfo(File file, int line, int character) {
        this.file = file;
        this.line = line;
        this.character = character;
    }

    @Override
    public int getLine() {
        return 0;
    }

    @Override
    public int getCharacter() {
        return 0;
    }

    @Override
    public String toString() {
        return file.getName() + ":[" + line + "," + character + "]";
    }
}
