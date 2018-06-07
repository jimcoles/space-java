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

    private Schema parseRootSchema;
    private ParseUnit fileParseUnit;

    ParsableChoice(Schema parseRootSchema) {
        this.parseRootSchema = parseRootSchema;
    }

    ParsableChoice(ParseUnit fileParseUnit) {
        this.fileParseUnit = fileParseUnit;
    }

    public boolean hasSchema() {
        return parseRootSchema != null;
    }

    public Schema getParseRootSchema() {
        if (parseRootSchema == null)
            throw new IllegalStateException("bad choice");
        return parseRootSchema;
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
