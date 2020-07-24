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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Everything the user/program accesses is a view including indexes.
 *
 * @author Jim Coles
 */
public class View {

    private ViewDefn viewDefn;
    private Collection<Tuple> items;

    public View(ViewDefn viewDefn) {
        this.viewDefn = viewDefn;
        this.items = new LinkedList<>();
    }

    public View addItem(Tuple tuple) {
        items.add(tuple);
        return this;
    }

    public Collection<Tuple> getItems() {
        return items;
    }

}
