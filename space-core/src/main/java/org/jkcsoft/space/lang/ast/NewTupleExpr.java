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
    private TupleValueList tupleValueList;

    public NewTupleExpr(SourceInfo sourceInfo, FullTypeRefImpl typeRef, TupleValueList tupleValueList) {
        super(sourceInfo);
        this.typeRef = typeRef;
        this.tupleValueList = tupleValueList;
        //
        if (typeRef != null)
            addChild(typeRef);

        addChild(tupleValueList);
    }

    public FullTypeRefImpl getTypeRef() {
        return typeRef;
    }

    public TupleValueList getTupleValueList() {
        return tupleValueList;
    }

    @Override
    public TypeDefn getDatumType() {
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
