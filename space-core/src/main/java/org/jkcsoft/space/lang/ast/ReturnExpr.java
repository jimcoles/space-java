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
 * AST rep of a return statement capturing the return type and the local var name to be
 * assigned to the return value holder.
 *
 * @author Jim Coles
 */
public class ReturnExpr extends Statement {

    private ValueExpr valueExpr;

    ReturnExpr(SourceInfo sourceInfo, ValueExpr valueExpr) {
        super(sourceInfo);
        this.valueExpr = valueExpr;
        //
        addChild((ModelElement) valueExpr);
    }

    public ValueExpr getValueExpr() {
        return valueExpr;
    }
}
