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
public class ValueSequenceLiteralExpr extends AbstractModelElement implements ValueExpr {

    private TypeRef typeRef;
    private String valueExpr;

    ValueSequenceLiteralExpr(SourceInfo sourceInfo, TypeRef typeRef, String valueExpr) {
        super(sourceInfo);
        this.typeRef = typeRef;
        this.valueExpr = valueExpr;
        //
        addChild(typeRef);
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    public String getStringValue() {
        return valueExpr;
    }

    @Override
    public String getDisplayName() {
        return valueExpr;
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
    public TypeDefn getDatumType() {
        return typeRef.getResolvedType();
    }

    @Override
    public boolean hasResolvedType() {
        return typeRef.hasResolvedType();
    }

    @Override
    public boolean isLiteral() {
        return true;
    }
}
