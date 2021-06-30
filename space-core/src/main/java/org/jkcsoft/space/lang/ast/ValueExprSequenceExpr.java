/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.LinkedList;
import java.util.List;

/**
 * The AST representation of a general sequence of value expressions.
 *
 * @author Jim Coles
 */
public class ValueExprSequenceExpr extends AbstractModelElement implements ValueExpr {

    private final TypeRef containedTypeRef;
    private final List<ValueExpr> valueExprs = new LinkedList<>();

    protected ValueExprSequenceExpr(SourceInfo sourceInfo, TypeRef containedTypeRef) {
        super(sourceInfo);
        this.containedTypeRef = containedTypeRef;
    }

    public ValueExprSequenceExpr addValueExpr(ValueExpr valueExpr) {
        valueExprs.add(valueExpr);
        //
        addChild(valueExpr);
        return this;
    }

    public TypeRef getContainedTypeRef() {
        return containedTypeRef;
    }

    public List<ValueExpr> getValueExprs() {
        return valueExprs;
    }

    @Override
    public boolean hasResolvedType() {
        return false;
    }

    @Override
    public TypeDefn getDatumType() {
        return containedTypeRef.getResolvedType().getSequenceOfType();
    }

    @Override
    public boolean hasRef() {
        return true;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }
}
