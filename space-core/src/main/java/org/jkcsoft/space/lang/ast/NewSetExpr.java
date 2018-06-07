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
    private List<NewObjectExpr> newObjectExprs = new LinkedList<>();

    NewSetExpr(SourceInfo sourceInfo, TypeRef tupleTypeRef) {
        super(sourceInfo);
        this.tupleTypeRef = tupleTypeRef;
        //
        addChild(tupleTypeRef);
    }

    public TypeRef getTupleTypeRef() {
        return tupleTypeRef;
    }

    public List<NewObjectExpr> getNewObjectExprs() {
        return newObjectExprs;
    }

    public void addNewObjectExpr(NewObjectExpr newObjectExpr) {
        newObjectExprs.add(newObjectExpr);
        //
        addChild(newObjectExpr);
    }

}
