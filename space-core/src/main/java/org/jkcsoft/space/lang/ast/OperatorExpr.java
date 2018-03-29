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

import java.util.LinkedList;
import java.util.List;

/**
 * {@link OperatorExpr}s are the heart of query criteria and equation
 * expressions.  They are expressions that evaluate to true or false
 * (or unknown?).  Expect a wide array of boolean operators such as
 * '==', 'AND', 'OR', '<', '>', 'contains', and other set-theoretic opers.
 * int == int
 * float == float
 * float < float
 * date < date
 * boolean == boolean
 */
public class OperatorExpr extends ModelElement implements ValueExpr {

    private OperEnum oper;
    /* could be one, two or more args.  Could be nested expressions
    or could be literals. For a given operator, the expression must
    return a value of the proper dimensionality and type.
    */
    private List<ValueExpr> args = new LinkedList<>();

    public OperatorExpr(SourceInfo sourceInfo, OperEnum oper, ValueExpr... args) {
        super(sourceInfo);
        this.oper = oper;
        for (ValueExpr arg : args) {
            this.args.add(arg);
            //
            addChild((ModelElement) arg);
        }
    }

    public OperEnum getOper() {
        return oper;
    }

    public ValueExpr getTheArg() {
        return args.get(0);
    }

    public ValueExpr getLeft() {
        return args.get(0);
    }

    public ValueExpr getRight() {
        return args.get(1);
    }

    public List<ValueExpr> getArgs() {
        return args;
    }

    @Override
    public String getDisplayName() {
        return oper.name();
    }
}
