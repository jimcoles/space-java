/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.SpaceActionDefn;

/**
 * One of these per invocation of a functions.  Holds info.
 *
 * @author Jim Coles
 * @deprecated
 */
public class Call {
    // defn
    private SpaceActionDefn callableDefn;  // defines argument space
    // instance
    private Call previous; // call stack
    private Space argumentSpace;
    private Space localSpace;
    private Space returnSpace;


    /** Limit constructor access to package-only. */
    private Call(SpaceActionDefn callableDefn, Call previous, Space argumentSpace) {
        this.callableDefn = callableDefn;
        this.previous = previous;
        this.argumentSpace = argumentSpace;
    }

    public Call getPrevious() {
        return previous;
    }

    public Space getLocalSpace() {
        return localSpace;
    }

    public void setLocalSpace(Space localSpace) {
        this.localSpace = localSpace;
    }

    public Space getReturnSpace() {
        return returnSpace;
    }

    public void setReturnSpace(Space returnSpace) {
        this.returnSpace = returnSpace;
    }
}
