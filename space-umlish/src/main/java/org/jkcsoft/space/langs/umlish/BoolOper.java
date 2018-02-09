/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.langmaps.umlish;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class BoolOper extends Operation {
    public static final BoolOper BOOL_AND = new BoolOper();
    public static final BoolOper BOOL_OR = new BoolOper();

    //----------------------------------------------------------------------------
    // Constructor(s)
    //----------------------------------------------------------------------------

    /**
     * Private constructor
     */
    private BoolOper() {
    }

    //----------------------------------------------------------------------------
    // Public methods
    //----------------------------------------------------------------------------

    /**
     * All BoolOperations take arguments and return a boolean.
     */
    public boolean eval(List args) {
        return true;
    }

    public boolean eval(Map args) {
        return true;
    }
}