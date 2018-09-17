/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.loader;

import org.jkcsoft.space.lang.ast.ParseUnit;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class FileLoadResults {

    private ParseUnit parseUnit;
    private List<AstLoadError> errors;

    public ParseUnit getParseUnit() {
        return parseUnit;
    }

    public void setParseUnit(ParseUnit parseUnit) {
        this.parseUnit = parseUnit;
    }

    public List<AstLoadError> getErrors() {
        initErrors();
        return errors;
    }

    public void addError(AstLoadError error) {
        initErrors();
        this.errors.add(error);
    }

    private void initErrors() {
        if (errors == null)
            errors = new LinkedList<>();
    }
}
