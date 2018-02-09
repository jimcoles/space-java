/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.langmaps.umlish;

import java.util.List;
import java.util.Vector;

public class AssocChain extends Vector {
    //-------------------------------------------------------------------
    // Private static vars
    //-------------------------------------------------------------------

    //-------------------------------------------------------------------
    // Private instance vars
    //-------------------------------------------------------------------

    //-------------------------------------------------------------------
    // Private constructor(s)
    //-------------------------------------------------------------------


    public AssocChain() {
        super();
    }

    public AssocChain(List list) {
        super(list);
    }

    //-------------------------------------------------------------------
    // Public methods
    //-------------------------------------------------------------------
    public void add(Association assoc) {
        super.add(assoc);
    }

    public void removeLast() {
        remove(size() - 1);
    }
}