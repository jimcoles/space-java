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
@GroupingNode
public class FunctionCallExpr extends ModelElement implements ValueExpr {

    /**
     * The name of some other named design-time thing such as a function.
     */
    private MetaReference<AbstractFunctionDefn> functionDefnRef;
    private List<ValueExpr> argumentExprs;

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

    public void setArgumentExprs(List<ValueExpr> argumentExprs) {
        this.argumentExprs = argumentExprs;
        //
        for (ValueExpr argumentExpr : argumentExprs) {
            addChild((ModelElement) argumentExpr);
            //
//            if (argumentExpr instanceof MetaReference) {
//                addReference((MetaReference) argumentExpr);
//            }
        }
    }

    public MetaReference<AbstractFunctionDefn> getFunctionDefnRef() {
        return functionDefnRef;
    }

    public List<ValueExpr> getArgumentExprs() {
        return argumentExprs;
    }

    @Override
    public String getText() {
        return "" + functionDefnRef;
    }
}
