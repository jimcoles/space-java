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

import java.util.List;

/**
 * {@link BooleanExpr}s are the heart of query criteria and equation
 * expressions.  They expressions that evaluate to true or false
 * (or unknown?).  Expect a wide array of boolean operators such as
 * '==', 'AND', 'OR', '<', '>', 'contains', and other set-theoretic opers.
 * int == int
 * float == float
 * float < float
 * date < date
 * boolean == boolean
 */
public class BooleanExpr implements ValueExpr {

    private BooleanOper oper;
    /* could be one, two or more args.  Could be nested expressions
    or could be literals. For a given operator, the expression must
    return a value of the proper dimensionality and type.
    */
    private ValueExpr left;
    private ValueExpr right;
}
