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
    private MetaReference<FunctionDefn> functionDefnRef;
    private TupleExpr argTupleExpr;
    private MetaReference argTupleRef;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public FunctionCallExpr setFunctionDefnRef(MetaReference functionDefnRef)
    {
        if (functionDefnRef == null) throw new RuntimeException("bug: function path null");
        //
        this.functionDefnRef = functionDefnRef;
        //
        addChild(this.functionDefnRef);
        //
        return this;
    }

    public MetaReference<FunctionDefn> getFunctionDefnRef() {
        return functionDefnRef;
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
        return "" + functionDefnRef;
    }
}
