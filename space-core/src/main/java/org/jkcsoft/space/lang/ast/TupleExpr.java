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
public class TupleExpr extends ModelElement implements ValueExpr {

    private List<ValueExpr> valueExprs;

    TupleExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public TupleExpr setValueExprs(List<ValueExpr> valueExprs) {
        this.valueExprs = valueExprs;
        //
        for (ValueExpr argumentExpr : valueExprs) {
            addChild((ModelElement) argumentExpr);
        }
        return this;
    }

    public List<ValueExpr> getValueExprs() {
        return valueExprs;
    }

}
