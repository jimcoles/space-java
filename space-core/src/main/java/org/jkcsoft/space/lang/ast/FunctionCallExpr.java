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

import java.util.List;

/**
 * The AST's representation of a call to an object.function() along with arguments expressions.
 *
 * @author Jim Coles
 */
public class FunctionCallExpr extends ModelElement implements ValueExpr {

    /**
     * The name of some other named design-time thing such as a function.
     */
    private MetaReference<AbstractFunctionDefn> functionDefnRef;
    private TupleExpr tupleExpr;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public FunctionCallExpr setFunctionDefnRef(SpacePathExpr functionPathExpr)
    {
        if (functionPathExpr == null) throw new RuntimeException("bug: function path null");
        //
        this.functionDefnRef = new MetaReference(functionPathExpr, MetaType.FUNCTION);
        //
        addChild(functionDefnRef);
        //
        return this;
    }

    public MetaReference<AbstractFunctionDefn> getFunctionDefnRef() {
        return functionDefnRef;
    }

    public TupleExpr getTupleExpr() {
        return tupleExpr;
    }

    public void setTupleExpr(TupleExpr tupleExpr) {
        this.tupleExpr = tupleExpr;
        //
        addChild(tupleExpr);
    }

    @Override
    public String getDisplayName() {
        return "" + functionDefnRef;
    }
}
