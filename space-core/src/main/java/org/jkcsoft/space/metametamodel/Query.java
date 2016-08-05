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

/**
 *
 */
public class Query {
    //----------------------------------------------------------------------
    // Private class vars
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------
    private QuerySelect _selNode = new QuerySelect();
    private Class _targetClass = null;
    private QueryFilter _filterNode = new QueryFilter();
    private QuerySort _sortNode = new QuerySort();

    //----------------------------------------------------------------------
    // Public constructor
    //----------------------------------------------------------------------
    public Query() {
    }

    //----------------------------------------------------------------------
    // Public methods
    //----------------------------------------------------------------------

    public void setTargetClass(Class cls) {
        _targetClass = cls;
        QueryFilterExpr defFilter = _getClassDefnFilter(_targetClass);
        if (defFilter != null) {
            QueryFilter filter = getFilter();
            QueryFilterExpr filterExpr = filter.getExpr();
            if (filterExpr == null)
                filter.setExpr(defFilter);
            else {
                filterExpr = new QueryFilterExpr(BoolOper.BOOL_AND, filterExpr, defFilter);
                filter.setExpr(filterExpr);
            }
        }
    }

    public Class getTargetClass() {
        return _targetClass;
    }

    public QuerySelect getSelect() {
        return _selNode;
    }

    public QueryFilter getFilter() {
        return _filterNode;
    }

    public QuerySort getSort() {
        return _sortNode;
    }

    private QueryFilterExpr combineFilters(BoolOper bool, QueryFilterExpr one, QueryFilterExpr two)
            throws Exception {
        if (one == null) return two;
        if (two == null) return one;
        return new QueryFilterExpr(bool, one, two);
    }


    public Query setScope(Class target, Class scopeType, long scopeID)
            throws Exception {
        return setScope(target, null, scopeType, scopeID);
    }

    public Query setScope(Class target, Class connectToType, Class scopeType, long scopeID)
            throws Exception {
        return setScope(target, connectToType, scopeType, new long[]{scopeID});
    }

    public Query setScope(Class target, Class connectToType, Class scopeType, long[] scopeIDs)
            throws Exception {
        return null;
    }

    private QueryFilterExpr _getClassDefnFilter(Class cls) {
        return null;
    }
}