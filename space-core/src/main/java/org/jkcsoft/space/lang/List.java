/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */


package org.jkcsoft.space.lang;

/**
 * Instance-level holder of sequence/list of items at the same meta level.
 * A List is a Relation that implicitly maintains a sequence number from
 * 1 to n for all Tuples.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class List extends Relation {
    //----------------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------------

    //----------------------------------------------------------------------------
    // Constructor(s) (private, package, protected, public)
    //----------------------------------------------------------------------------

    /**
     * Constructor...
     */
    public List() {
    }

    //----------------------------------------------------------------------------
    // Public methods - accessors, mutators, other
    //----------------------------------------------------------------------------
    public Iterator iterator() {
        return null;
    }

    //----------------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------------

}
