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
public class NewTupleExpr extends ModelElement implements ValueExpr {

    private TypeRefImpl typeRef;
    private TupleExpr tupleExpr;

    public NewTupleExpr(SourceInfo sourceInfo, TypeRefImpl typeRef, TupleExpr tupleExpr) {
        super(sourceInfo);
        this.typeRef = typeRef;
        this.tupleExpr = tupleExpr;
        //
        addChild(typeRef);
        addChild(tupleExpr);
    }

    public TypeRefImpl getTypeRef() {
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
    public boolean hasNameRef() {
        return false;
    }

    @Override
    public NameRef getNameRef() {
        return null;
    }
}
