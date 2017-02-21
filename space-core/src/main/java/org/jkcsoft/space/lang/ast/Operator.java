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
 * @author Jim Coles
 */
public abstract class Operator {

    private int minArgs;
    private int maxArgs;

    Operator() {
        this(1);
    }

    Operator(int maxArgs) {
        this(1, maxArgs);
    }

    Operator(int minArgs, int maxArgs) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    public boolean isMaxInf() {
        return false;
    }

    public static class NewSpaceOper extends Operator {
        NewSpaceOper() {
        }
    }

    public static class NewTupleOper extends Operator {
        NewTupleOper() {
            super(2, 2);
        }
    }

}
