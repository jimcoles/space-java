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
public class NewSetExpr extends AbstractModelElement implements ValueExpr {

    private TypeRef tupleTypeRef;
    private List<NewTupleExpr> newTupleExprs = new LinkedList<>();

    NewSetExpr(SourceInfo sourceInfo, TypeRef tupleTypeRef) {
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
    public TypeDefn getDatumType() {
        return tupleTypeRef.getResolvedType();
    }

    @Override
    public boolean hasResolvedType() {
        return tupleTypeRef != null && tupleTypeRef.hasResolvedType();
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
