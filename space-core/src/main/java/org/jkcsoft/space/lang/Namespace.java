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
 * ModelElements that are referenceable by name must implement Namespace.
 *
 * @author Jim Coles
 * @version 1.0
 */
public interface Namespace {
    //----------------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------------

    //----------------------------------------------------------------------------
    // Public methods - accessors, mutators, other
    //----------------------------------------------------------------------------
    public String getCodeName();

    //----------------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------------

}
