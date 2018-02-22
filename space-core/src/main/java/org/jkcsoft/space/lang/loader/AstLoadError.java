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

import org.jkcsoft.space.lang.ast.SourceInfo;

/**
 * @author Jim Coles
 */
public class AstLoadError {

    private String message;
    private SourceInfo sourceInfo;

    public AstLoadError(String message, SourceInfo sourceInfo) {
        this.message = message;
        this.sourceInfo = sourceInfo;
    }

    public String getMessage() {
        return message;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
