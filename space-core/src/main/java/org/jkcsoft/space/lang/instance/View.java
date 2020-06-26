/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.ViewDefn;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Everything the user/program accesses is a view including indexes.
 *
 * @author Jim Coles
 */
public class View implements Space {

    private ViewDefn viewDefn;
    private List<Tuple> items;

    public View(ViewDefn viewDefn) {
        this.viewDefn = viewDefn;
        this.items = new LinkedList<>();
    }

    public View addItem(Tuple tuple) {
        items.add(tuple);
        return this;
    }

    public List<Tuple> getItems() {
        return items;
    }

    @Override
    public Set<TypeDefn> getComplexTypeDefs() {
        return null;
    }

    @Override
    public List<View> getViews() {
        return null;
    }
}
