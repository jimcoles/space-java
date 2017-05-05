/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
    private ValueExpr   functionPathExpr;
    private ValueExpr[] argumentExprs;

    /**
     *
     * @param name The (optional) name of the expression itself.  Space allows any expression
     *             to be named for use in refactoring and version change management.
     * @param functionPathExpr This expression must evaluate to the meta object (reference)
     *                         representing the function of operation being invoked.
     *                         Actions invoked
     *                         can only be the space path operations such as the navigation
     *                         operator.
     * @param argumentExprs Nested expression that evaluate at runtime to the values
     *                      used in this action call.
     */
    ActionCallExpr(String name, ValueExpr functionPathExpr, ValueExpr ... argumentExprs) {
        super(name);
        this.functionPathExpr = functionPathExpr;
        this.argumentExprs = argumentExprs;
    }

    public ValueExpr getFunctionPathExpr() {
        return functionPathExpr;
    }

    public ValueExpr[] getArgumentExprs() {
        return argumentExprs;
    }

    @Override
    public String toString() {
        return "call expression to " + functionPathExpr + "()";
    }
}
