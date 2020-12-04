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
public class SequenceLiteralExpr extends AbstractModelElement implements ValueExpr {

    private TypeRefImpl typeRef;
    private String valueExpr;

    SequenceLiteralExpr(SourceInfo sourceInfo, TypeRefImpl typeRef, String valueExpr) {
        super(sourceInfo);
        this.typeRef = typeRef;
        this.valueExpr = valueExpr;
        //
        addChild(typeRef);
    }

    public TypeRefImpl getTypeRef() {
        return typeRef;
    }

    public String getValueExpr() {
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
        return typeRef.getDatumType();
    }

    @Override
    public boolean hasResolvedType() {
        return typeRef.hasResolvedType();
    }
}
