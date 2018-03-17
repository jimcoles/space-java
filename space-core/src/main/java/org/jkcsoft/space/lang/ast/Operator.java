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

    public abstract Object exec(Object ... args);

    public static class NewSpaceOper extends Operator {
        NewSpaceOper() {
        }

        @Override
        public Object exec(Object... args) {
            return null;
        }
    }

    public static class NewTupleOper extends Operator {
        NewTupleOper() {
            super(2, 2);
        }

        @Override
        public Object exec(Object... args) {
            return null;
        }
    }

}
