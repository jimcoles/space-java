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
 * A Space is the central notion of the Space language.  That is, "everything is a Space".
 * A Space, aka Relation, is an instance-level
 * notion of a collection of Tuples the values of which are controlled by a Space definition.
 * It is essentially similar to a collection in
 * Java or an RDB table. A Space may be derived from other Spaces.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Space {

    private SpaceDefn definition;

    public Space() {

    }

    public Iterator getIterator() {
        Iterator iter = null;

        return iter;
    }

}
