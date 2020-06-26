/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * Represents a Tuple update expression.
 *
 * @author Jim Coles
 */
public class UpdateExpr extends AssignmentExpr {

    public UpdateExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

}
