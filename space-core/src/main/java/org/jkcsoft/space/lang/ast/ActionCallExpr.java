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
public class ActionCallExpr extends ModelElement implements ValueExpr {

    /** The name of some other named design-time thing such as a function.
     */
    private MetaReference<SpaceActionDefn>   functionRef;
    private ValueExpr[] argumentExprs;

    /**
     * Represents the invocation of a function.
     *
     * @param functionPathExpr This expression must evaluate to the meta object (reference)
     *                         representing the function of operation being invoked.
     *                         Actions invoked
     *                         can only be the space path operations such as the navigation
     *                         operator.
     * @param argumentExprs Nested expression that evaluate at runtime to the values
     *                      used in this action call.
     */
    ActionCallExpr(SourceInfo sourceInfo, SpacePathExpr functionPathExpr, ValueExpr ... argumentExprs) {
        super(sourceInfo);
        if (functionPathExpr == null) throw new RuntimeException("bug: function path null");
        this.functionRef = new MetaReference(functionPathExpr);
        this.addReference(functionRef);
        this.argumentExprs = argumentExprs;
    }

    public MetaReference<SpaceActionDefn> getFunctionRef() {
        return functionRef;
    }

    public ValueExpr[] getArgumentExprs() {
        return argumentExprs;
    }

    public String toLogString() {
        return "call expression to " + functionRef + "()";
    }
}
