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

import java.util.List;
import java.util.Vector;

public class QuerySelect {
    //-----------------------------------------------------------------
    // Private instance vars
    //-----------------------------------------------------------------
    private List _items = new Vector();
    private boolean _useDistinct = false;

    //-----------------------------------------------------------------
    // Public instance methods
    //-----------------------------------------------------------------
    public List getAttrs() {
        return _items;
    }

    public void addAttr(QueryAttrRef attr) {
        _items.add(new QuerySelectItem(attr, null));
    }

    public void addAttr(QueryAttrRef attr, String alias) {
        _items.add(new QuerySelectItem(attr, alias));
    }

    public void addLiteral(String value, String alias) {
        _items.add(new QuerySelectItem(value, alias));
    }

    public QuerySelect setUseDistinct(boolean huh) {
        _useDistinct = huh;
        return this;
    }

    public boolean getUseDistinct() {
        return _useDistinct;
    }
}
