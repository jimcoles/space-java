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

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class NewSetExpr extends ModelElement implements ValueExpr {

    private TypeRef tupleTypeRef;
    private List<NewTupleExpr> newTupleExprs = new LinkedList<>();

    NewSetExpr(SourceInfo sourceInfo, TypeRefImpl tupleTypeRef) {
        super(sourceInfo);
        this.tupleTypeRef = tupleTypeRef;
        //
        addChild(tupleTypeRef);
    }

    public TypeRef getTupleTypeRef() {
        return tupleTypeRef;
    }

    public List<NewTupleExpr> getNewTupleExprs() {
        return newTupleExprs;
    }

    public void addNewObjectExpr(NewTupleExpr newTupleExpr) {
        newTupleExprs.add(newTupleExpr);
        //
        addChild(newTupleExpr);
    }

    @Override
    public DatumType getDatumType() {
        return tupleTypeRef.getResolvedType();
    }

    @Override
    public boolean hasNameRef() {
        return false;
    }

    @Override
    public NameRef getNameRef() {
        return null;
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

}
