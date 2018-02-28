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
 * Represents a call to a function along with arguments expressions.
 *
 * @author Jim Coles
 */
@GroupingNode
public class FunctionCallExpr extends ModelElement implements ValueExpr {

    /** The name of some other named design-time thing such as a function.
     */
    private MetaReference<AbstractFunctionDefn>   functionRef;
    private ValueExpr[] argumentExprs;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public FunctionCallExpr setFunctionRef(SpacePathExpr functionPathExpr)
    {
        if (functionPathExpr == null) throw new RuntimeException("bug: function path null");
        //
        this.functionRef = new MetaReference(functionPathExpr);
        addChild(functionPathExpr);
        //
        addReference(functionRef);
        //
        return this;
    }

    public void setArgumentExprs(ValueExpr[] argumentExprs) {
        this.argumentExprs = argumentExprs;
        //
        for (ValueExpr argumentExpr : argumentExprs) {
            addChild((ModelElement) argumentExpr);
            //
            if (argumentExpr instanceof MetaReference) {
                addReference((MetaReference) argumentExpr);
            }
        }
    }

    public MetaReference<AbstractFunctionDefn> getFunctionRef() {
        return functionRef;
    }

    public ValueExpr[] getArgumentExprs() {
        return argumentExprs;
    }

    @Override
    public String getText() {
        return "" + functionRef;
    }
}
