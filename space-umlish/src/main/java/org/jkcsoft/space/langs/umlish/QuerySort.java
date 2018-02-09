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


import java.util.List;
import java.util.Vector;

public class QuerySort {
    private List _items = new Vector();

    public QuerySortItem addSortItem(QueryAttrRef attr, SortDir dir) {
        QuerySortItem item = new QuerySortItem(attr, dir);
        _items.add(item);
        return item;
    }

    public List getSortItems() {
        return _items;
    }
}
