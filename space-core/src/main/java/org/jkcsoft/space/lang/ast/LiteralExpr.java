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
 * A instance of this type is created for every occurrence of a literal string
 * in a source file.
 *
 * @author Jim Coles
 */
public class LiteralExpr extends ModelElement implements ValueExpr {

    private PrimitiveType   primitiveType;
    private String valueExpr;

    LiteralExpr(SourceInfo sourceInfo, PrimitiveType primitiveType, String valueExpr) {
        super(sourceInfo);
        this.primitiveType = primitiveType;
        this.valueExpr = valueExpr;
    }

    public PrimitiveType getPrimitiveType() {
        return primitiveType;
    }

    public String getValueExpr() {
        return valueExpr;
    }

    @Override
    public String getText() {
        return valueExpr;
    }
}
