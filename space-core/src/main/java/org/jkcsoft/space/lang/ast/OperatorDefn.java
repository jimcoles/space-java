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
 * Like a {@link FunctionDefn} but applies to Operator like numeric addition and
 * boolean-valued logic opers AND, and OR.
 *
 * @author Jim Coles
 */
public abstract class OperatorDefn {

    private DatumType argType;
    private DatumType returnType;
    private int minArgs;
    private int maxArgs;

    OperatorDefn() {
        this(1);
    }

    OperatorDefn(int maxArgs) {
        this(1, maxArgs);
    }

    OperatorDefn(int minArgs, int maxArgs) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public boolean isMaxInf() {
        return false;
    }

    public DatumType getArgType() {
        return argType;
    }

    public DatumType getReturnType() {
        return returnType;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }
}
