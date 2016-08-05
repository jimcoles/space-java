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
 * Encapsulates an entire executable system as defined by ROML definition elements
 * (ModelElements) and associated instances.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class RomlProgram extends ModelElement {
    //----------------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------------
    Relation _relationDefns;
    Relation _assocDefns;
    Relation _actionSequenceDefns;

    // TODO: indexes for fast lookup

    //----------------------------------------------------------------------------
    // Constructor(s) (private, package, protected, public)
    //----------------------------------------------------------------------------

    /**
     * Constructor...
     */
    public RomlProgram() {
    }

    //----------------------------------------------------------------------------
    // Public methods - accessors, mutators, other
    //----------------------------------------------------------------------------


    //----------------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------------

}
