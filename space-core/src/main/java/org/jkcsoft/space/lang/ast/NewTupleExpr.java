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
 * Creates new Tuples, Spaces(Sets), Sequences.
 */
public class NewTupleExpr extends AbstractModelElement implements ValueExpr {

    private FullTypeRefImpl typeRef;
    private TupleExpr tupleExpr;

    public NewTupleExpr(SourceInfo sourceInfo, FullTypeRefImpl typeRef, TupleExpr tupleExpr) {
        super(sourceInfo);
        this.typeRef = typeRef;
        this.tupleExpr = tupleExpr;
        //
        addChild(typeRef);
        addChild(tupleExpr);
    }

    public FullTypeRefImpl getTypeRef() {
        return typeRef;
    }

    public TupleExpr getTupleExpr() {
        return tupleExpr;
    }

    @Override
    public DatumType getDatumType() {
        return typeRef.getDatumType();
    }

    @Override
    public boolean hasResolvedType() {
        return typeRef != null && typeRef.hasResolvedType();
    }

    @Override
    public boolean hasRef() {
        return false;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }
}
