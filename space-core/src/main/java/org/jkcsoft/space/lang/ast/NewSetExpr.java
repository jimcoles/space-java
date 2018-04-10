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

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class NewSetExpr extends ModelElement implements ValueExpr {
    private MetaReference<SpaceTypeDefn> tupleTypeRef;
    private List<NewObjectExpr> newObjectExprs = new LinkedList<>();

    NewSetExpr(SourceInfo sourceInfo, SpacePathExpr tupleTypeRef) {
        super(sourceInfo);
        this.tupleTypeRef = new MetaReference<>(tupleTypeRef, MetaType.TYPE);
        //
        addChild(tupleTypeRef);
    }

    public MetaReference<SpaceTypeDefn> getTupleTypeRef() {
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
