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

import java.util.Map;

/**
 * The instance-level notion corresponding essentially to a row of data or to
 * an object in OOP.
 *
 * @author J. Coles
 * @version 1.0
 */
public abstract class Type {
    //----------------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------------

    //----------------------------------------------------------------------------
    // Constructor(s) (private, package, protected, public)
    //----------------------------------------------------------------------------

    /**
     * Constructor...
     */
    public Type() {
    }

    //----------------------------------------------------------------------------
    // Public methods - accessors, mutators, other
    //----------------------------------------------------------------------------

    //---- <Accessors and Mutators> ----------------------------------------------

    //---- </Accessors and Mutators> ----------------------------------------------

    /**
     * Get a Tuple value of Property named <code>name</code>
     */
    public abstract Object getValue(String codeName);

    public abstract Map getValueMap();

    //----------------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------------

}
