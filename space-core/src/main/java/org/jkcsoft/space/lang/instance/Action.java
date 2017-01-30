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

import org.jkcsoft.space.lang.ast.SpaceActionDefn;

/**
 * The instance level state for the invocation of an Action Definition.  Might be
 * a top-level function call or just part of an action tree within a function.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Action {

    private SpaceActionDefn actionDefn;
    //
    private Space context;
    private Space returnSpace;

    /**
     * @param spcContext Could be a local function space or the argument space
     *                   for a function call.
     */
    Action(Space spcContext, SpaceActionDefn actionDefn) {
        this.actionDefn = actionDefn;
        this.context = spcContext;
    }

    public void setReturnSpace(Space returnSpace) {
        this.returnSpace = returnSpace;
    }

    public Space getReturnSpace() {
        return returnSpace;
    }
}
