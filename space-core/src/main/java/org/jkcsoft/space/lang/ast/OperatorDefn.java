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
 * Possibly only used by the language itself, i.e., we probably won't
 * allow users to define new operators.
 *
 * An Operator (Defn) is like an Function (Defn) exception that Operator arguments
 * are not named.
 *
 * @author Jim Coles
 */
public abstract class OperatorDefn extends ModelElement {

    private DatumType datumType;
    private int minArgs;
    private int maxArgs;

    OperatorDefn(SourceInfo sourceInfo, DatumType datumType, int numArgs) {
        this(sourceInfo, datumType, numArgs, numArgs);
    }

    OperatorDefn(SourceInfo sourceInfo, DatumType datumType, int minArgs, int maxArgs) {
        super(sourceInfo);
        this.datumType = datumType;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public boolean isMaxInf() {
        return false;
    }

    public DatumType getDatumType() {
        return datumType;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }
}
