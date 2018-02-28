/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.SourceInfo;

/**
 * Holds detailed error info.  Used by {@link RuntimeException} and by loaders
 * and linkers.
 *
 * @author Jim Coles
 */
public class RuntimeError {

    private SourceInfo sourceInfo;
    private String message;
    private int errorCode;

    public RuntimeError(SourceInfo sourceInfo, int errorCode, String message) {
        this.sourceInfo = sourceInfo;
        this.message = message;
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "error " + (errorCode != 0 ? "<" + errorCode + ">" : "") + sourceInfo + " " + message ;
    }
}
