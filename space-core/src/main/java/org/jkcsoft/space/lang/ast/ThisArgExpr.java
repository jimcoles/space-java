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
 * Evaluates to the current argument tuple.
 *
 * @author Jim Coles
 */
public class ThisArgExpr extends ModelElement implements ValueExpr {

    ThisArgExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    @Override
    public DatumType getDatumType() {
        return null;
    }
}
