/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.langmaps.umlish;

/**
 * Implements IQFilter for the PMC query subsystem.
 */
public class QueryFilter {
    //----------------------------------------------------------------------
    // Private class vars
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------
    private QueryFilterExpr _expr = null;

    //----------------------------------------------------------------------
    // Constructor(s)
    //----------------------------------------------------------------------
    QueryFilter() {
    }

    //----------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------
    public void setExpr(QueryFilterExpr expr) {
        _expr = expr;
    }

    public QueryFilterExpr getExpr() {
        return _expr;
    }
}
