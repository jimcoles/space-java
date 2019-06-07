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
public class OperLookupExpr extends AbstractModelElement implements ValueExpr {

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
    public boolean hasRef() {
        return false;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }

    @Override
    public boolean hasResolvedType() {
        return false;
    }
}
