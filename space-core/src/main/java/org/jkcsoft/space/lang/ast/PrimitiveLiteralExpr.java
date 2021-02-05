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
public class PrimitiveLiteralExpr extends AbstractModelElement implements ValueExpr {

    private NumPrimitiveTypeDefn typeDefn;
    private String valueExpr;

    PrimitiveLiteralExpr(SourceInfo sourceInfo, NumPrimitiveTypeDefn primitiveTypeDefn, String valueExpr) {
        super(sourceInfo);
        this.typeDefn = primitiveTypeDefn;
        this.valueExpr = valueExpr;
    }

    public NumPrimitiveTypeDefn getTypeDefn() {
        return typeDefn;
    }

    public String getStringValue() {
        return valueExpr;
    }

    @Override
    public String getDisplayName() {
        return valueExpr;
    }

    @Override
    public TypeDefn getDatumType() {
        return typeDefn;
    }

    @Override
    public boolean hasResolvedType() {
        return typeDefn != null;
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
    public boolean isLiteral() {
        return true;
    }
}
