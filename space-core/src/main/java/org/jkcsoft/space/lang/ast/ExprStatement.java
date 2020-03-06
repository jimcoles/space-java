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
 * A generic expression container.  Contains a value-returning expression
 * that must be evaluated such as a function call, or assignment.
 *
 * @author Jim Coles
 */
public class ExprStatement<T extends ValueExpr> extends AbstractModelElement implements Statement {

    private T expression;

    public ExprStatement(T expression) {
        super(((ModelElement) expression).getSourceInfo());
        this.expression = expression;
    }

    public T getExpression() {
        return expression;
    }
}
