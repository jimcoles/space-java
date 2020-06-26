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
 * Like a {@link SpaceFunctionDefn} but applies to Operator like numeric addition and
 * boolean-valued logic opers AND, and OR.
 *
 * @author Jim Coles
 */
public abstract class OperatorDefn {

    private TypeDefn argType;
    private TypeDefn returnType;
    private int minArgs;
    private int maxArgs;

    OperatorDefn() {
        this(2);
    }

    OperatorDefn(int nAry) {
        this(nAry, nAry);
    }

    OperatorDefn(int minArgs, int maxArgs) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public boolean isMaxInf() {
        return false;
    }

    public TypeDefn getArgType() {
        return argType;
    }

    public TypeDefn getReturnType() {
        return returnType;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }
}
