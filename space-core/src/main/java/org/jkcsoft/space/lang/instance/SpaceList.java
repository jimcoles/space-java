/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */


package org.jkcsoft.space.lang.instance;

/**
 * NOTE: I don't know if we really need this.  For now, a Space is inherently
 * a "list", i.e., maintains sequence of Tuples.
 *
 * Instance-level holder of sequence/list of items at the same meta level.
 * A List is a Relation that implicitly maintains a sequence number from
 * 1 to n for all Tuples.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceList {


    public SpaceList() {
    }

    public SpaceIterator iterator() {
        return null;
    }


}
