/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.langmaps.umlish;


/**
 * @author J. Coles
 * @version 1.0
 */
public class QueryExpr {
    //----------------------------------------------------------------------
    // Private class vars
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------
    private Operation _oper = null;
    private Object _leftArg = null;
    private Object _rightArg = null;

    //----------------------------------------------------------------------
    // Constructor(s)
    //----------------------------------------------------------------------
    public QueryExpr() {
    }

    public QueryExpr(Operation oper, Object left, Object right) {
        setOper(oper);
        setLeft(left);
        setRight(right);
    }

    //----------------------------------------------------------------------
    // Public Methods
    //----------------------------------------------------------------------

    public Object getLeft() {
        return _leftArg;
    }

    public Object getRight() {
        return _rightArg;
    }

    public Operation getOper() {
        return _oper;
    }

    public void setOper(Operation oper) {
        _oper = oper;
    }

    public void setLeft(Object left) {
        _leftArg = left;
    }

    public void setRight(Object right) {
        _rightArg = right;
    }
}
