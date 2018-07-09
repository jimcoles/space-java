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

/**
 * @author Jim Coles
 */
public class ParsableChoice {

    private Directory parseRootDirectory;
    private ParseUnit fileParseUnit;

    ParsableChoice(Directory parseRootDirectory) {
        this.parseRootDirectory = parseRootDirectory;
    }

    ParsableChoice(ParseUnit fileParseUnit) {
        this.fileParseUnit = fileParseUnit;
    }

    public boolean hasParseRootDir() {
        return parseRootDirectory != null;
    }

    public Directory getParseRootDirectory() {
        if (parseRootDirectory == null)
            throw new IllegalStateException("bad choice");
        return parseRootDirectory;
    }

    public boolean hasParseUnit() {
        return fileParseUnit != null;
    }

    public ParseUnit getFileParseUnit() {
        if (fileParseUnit == null)
            throw new IllegalStateException("bad choice");
        return fileParseUnit;
    }
}
