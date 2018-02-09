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

public class QueryFilterExpr extends QueryExpr {
    //----------------------------------------------------------------------
    // Private class vars
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------
    private boolean _syn = false;

    //----------------------------------------------------------------------
    // Constructor(s)
    //----------------------------------------------------------------------
    public QueryFilterExpr(Operation oper, Object left, Object right) {
        super(oper, left, right);
    }

    public QueryFilterExpr(Operation oper, Object left, Object right, boolean syn) throws Exception {
        this(oper, left, right);
        _syn = syn;
        if (syn && right instanceof String) {
            QueryFilterExpr newLeft = new QueryFilterExpr(oper, left, right);
            QueryFilterExpr newRight = null;
        }
    }

    public boolean useSynonyms() {
        return _syn;
    }
}
