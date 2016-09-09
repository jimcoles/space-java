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

public class QuerySortItem {
    private SortDir _dir = null;
    private QueryAttrRef _attr = null;

    QuerySortItem(QueryAttrRef attr, SortDir dir) {
        _attr = attr;
        _dir = dir;
    }

    public SortDir getDir() {
        return _dir;
    }

    public QueryAttrRef getAttrRef() {
        return _attr;
    }
}
