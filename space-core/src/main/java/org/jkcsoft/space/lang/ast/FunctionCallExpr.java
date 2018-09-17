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
 * The AST's representation of a call to an object.function() along with arguments expressions.
 *
 * @author Jim Coles
 */
public class FunctionCallExpr extends ModelElement implements ValueExpr {

    /**
     * The name of some other named design-time thing such as a function.
     */
    private MetaReference<SpaceFunctionDefn> functionRef;
    private TupleExpr argTupleExpr;
    private MetaReference argTupleRef;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public FunctionCallExpr setFunctionRef(MetaReference functionRef)
    {
        if (functionRef == null) throw new RuntimeException("bug: function path null");
        //
        this.functionRef = functionRef;
        //
        addChild(this.functionRef);
        //
        return this;
    }

    public MetaReference getFunctionRef() {
        return functionRef;
    }

    public TupleExpr getArgTupleExpr() {
        return argTupleExpr;
    }

    public void setArgTupleExpr(TupleExpr argTupleExpr) {
        this.argTupleExpr = argTupleExpr;
        //
        addChild(argTupleExpr);
    }

    public void setArgTupleRef(MetaReference argTupleRef) {
        this.argTupleRef = argTupleRef;
        //
        addChild(argTupleRef);
    }

    @Override
    public String getDisplayName() {
        return functionRef.getDisplayName();
    }
}
