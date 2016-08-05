/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.metametamodel;

public class QuerySelectItem {
    //-----------------------------------------------------------------
    // Public instance methods
    //-----------------------------------------------------------------
    private Object _attr = null;
    private String _alias = null;

    //-----------------------------------------------------------------
    // Constructor(s)
    //-----------------------------------------------------------------
    QuerySelectItem(QueryAttrRef attr, String alias) {
        _attr = attr;
        _alias = alias;
    }

    QuerySelectItem(Object attr, String alias) {
        _attr = attr;
        _alias = alias;
    }

    //-----------------------------------------------------------------
    // Public instance methods
    //-----------------------------------------------------------------

    public Object getAttr() {
        return _attr;
    }

    public String getAlias() {
        return _alias;
    }
}
