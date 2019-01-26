/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * A synonym-based reference, e.g., 'this'. Not quite the same as a
 * {@link NameRefExpr}.
 *
 * @author Jim Coles
 */
public class SynRefExpr extends RefExprImpl implements TypedExpr {

    public SynRefExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    @Override
    public boolean hasNameRef() {
        return false;
    }

    @Override
    public NameRef getNameRef() {
        return null;
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }
}
