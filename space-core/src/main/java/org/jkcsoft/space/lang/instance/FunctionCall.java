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

import org.jkcsoft.space.lang.ast.FunctionDefn;

/**
 * The instance level state for the invocation of an Action Definition.  Might be
 * a top-level function call or just part of an action tree within a function.
 *
 * @author J. Coles
 * @version 1.0
 */
public class FunctionCall {

    private FunctionDefn actionDefn;
    //
    private Tuple ctxTuple;
    private Tuple argTuple;
    private Space returnSpace;

    /**
     * @param ctxTuple Could be a local function space or the argument space
     *                   for a function call.
     */
    FunctionCall(Tuple ctxTuple, FunctionDefn actionDefn, Tuple argTuple) {
        this.actionDefn = actionDefn;
        this.ctxTuple = ctxTuple;
        this.argTuple = argTuple;
    }

    public void setReturnSpace(Space returnSpace) {
        this.returnSpace = returnSpace;
    }

    public Space getReturnValue() {
        return returnSpace;
    }

    public FunctionDefn getActionDefn() {
        return actionDefn;
    }

    public Tuple getCtxTuple() {
        return ctxTuple;
    }

    public Tuple getArgTuple() {
        return argTuple;
    }

    public Space getReturnSpace() {
        return returnSpace;
    }
}
