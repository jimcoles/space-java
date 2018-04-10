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
 * Creates new Tuples, Spaces(Sets), Sequences.
 */
public class NewObjectExpr extends ModelElement implements ValueExpr {

    private MetaReference<SpaceTypeDefn> typeRef;
    private TupleExpr tupleExpr;

    public NewObjectExpr(SourceInfo sourceInfo, SpacePathExpr typeRefPathExpr, TupleExpr tupleExpr) {
        super(sourceInfo);
        this.typeRef = new MetaReference<>(typeRefPathExpr, MetaType.TYPE);
        this.tupleExpr = tupleExpr;
        //
        addChild(typeRef);
        addChild(tupleExpr);
    }

    public MetaReference<SpaceTypeDefn> getTypeRef() {
        return typeRef;
    }

    public TupleExpr getTupleExpr() {
        return tupleExpr;
    }
}
