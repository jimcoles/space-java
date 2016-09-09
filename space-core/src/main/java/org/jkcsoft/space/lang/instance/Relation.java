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

/**
 * The central notion of Space.  A Relation is an instance-level notion of a collection
 * of Tuples.  It is essentially similar to a collection in Java or an RDB table.  A Relation in
 * general is computed from Entity (persisted) Relations based on the
 * Relation's TypeDefn.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Relation {


    public Relation() {
    }



    public Iterator getIterator() {
        Iterator iter = null;

        return iter;
    }


}
