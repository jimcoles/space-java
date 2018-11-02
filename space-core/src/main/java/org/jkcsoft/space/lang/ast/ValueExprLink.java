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
 * Encapsulates a single part of value expression in an {@link ExpressionChain}.
 *
 * @author Jim Coles
 */
public class ValueExprLink<T extends NamedElement> extends ExprLink<T> {

    //    private MetaReference parentPath;
    private ValueExpr valueExpr; // Any AST value expression such as a Function Call Expr.

    public ValueExprLink(ValueExpr valueExpr) {
        super(((ModelElement) valueExpr).getSourceInfo());
        this.valueExpr = valueExpr;
    }

    public boolean isWildcard() {
        return (this.getExpression() instanceof NamePartExpr
            && ((NamePartExpr) this.getExpression()).getNameExpr().equals("*"));
    }

    public boolean isValueExpr() {
        return true;
    }

    public ValueExpr getValueExpr() {
        return valueExpr;
    }

    public ModelElement getExpression() {
        return ((ModelElement) valueExpr);
    }

}
