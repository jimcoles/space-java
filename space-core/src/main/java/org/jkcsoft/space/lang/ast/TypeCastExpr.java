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
public class TypeCastExpr extends ModelElement {

    private TypeRef targetTypeRef;
    private ValueExpr valueExpr;
    private boolean implicit;

    protected TypeCastExpr(SourceInfo sourceInfo, TypeRef targetTypeRef, ValueExpr valueExpr, boolean implicit) {
        super(sourceInfo);
        this.targetTypeRef = targetTypeRef;
        this.valueExpr = valueExpr;
        this.implicit = implicit;
    }

    protected TypeCastExpr(SourceInfo sourceInfo, TypeRef targetTypeRef, ValueExpr valueExpr) {
        this(sourceInfo, targetTypeRef, valueExpr, false);
    }

    public TypeRef getTargetTypeRef() {
        return targetTypeRef;
    }

    public ValueExpr getValueExpr() {
        return valueExpr;
    }

    public boolean isImplicit() {
        return implicit;
    }
}
