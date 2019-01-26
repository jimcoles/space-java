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
public class OperLookupExpr extends ModelElement implements ValueExpr {

    private OperEnum operLookupKey;

    public OperLookupExpr(SourceInfo sourceInfo, OperEnum operLookupKey) {
        super(sourceInfo);
        this.operLookupKey = operLookupKey;
    }

    @Override
    public DatumType getDatumType() {
        return null;
    }

    @Override
    public boolean hasNameRef() {
        return false;
    }

    @Override
    public NameRef getNameRef() {
        return null;
    }
}
