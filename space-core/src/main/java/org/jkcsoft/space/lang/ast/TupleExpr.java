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

import java.util.List;

/**
 * A list of value expressions, or, a set of assignment expressions.
 *
 * @author Jim Coles
 */
public class TupleExpr extends AbstractModelElement implements ValueExpr {

    private List<ValueExpr> valueExprs;
    private ComplexType implicitType;

    TupleExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public TupleExpr setValueExprs(List<ValueExpr> valueExprs) {
        this.valueExprs = valueExprs;
        //
        for (ValueExpr argumentExpr : valueExprs) {
            addChild((AbstractModelElement) argumentExpr);
        }
        return this;
    }

    public List<ValueExpr> getValueExprs() {
        return valueExprs;
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
    public DatumType getDatumType() {
        return implicitType;
    }
}
