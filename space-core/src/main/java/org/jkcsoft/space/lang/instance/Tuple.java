/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.SpaceDefn;

/**
 * Conceptually, an element of a Relation. Much like a row in a JDBC recordset.<br><br>
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Tuple {
    Space _relation;
    SpaceDefn _relationDefn;
    Object[] _values;


    public Tuple(Space relation) {
        _relation = relation;
    }


    public Space getRelation() {
        return _relation;
    }

    /**
     * Return value of Tuple variable with <code>name</code>
     */
    public Object get(String name) {
        return null;
    }


}
