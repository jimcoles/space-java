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

import org.jkcsoft.space.lang.ast.AbstractFunctionDefn;
import org.jkcsoft.space.lang.ast.FunctionCallExpr;
import org.jkcsoft.space.lang.ast.FunctionDefn;

import java.util.Stack;

/**
 * The instance level state for the invocation of an Action Definition.  Might be
 * a top-level function call or nested.  Every function call is pushed onto the
 * call stack.  The FunctionCallContext objects holds data specific to this invocation.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class FunctionCallContext extends AbstractExeContext implements ExeContext {

    private Tuple ctxObject;
    private Tuple argTuple;
    private Stack<BlockContext> blockContextStack = new Stack<>();
    private Assignable returnValue;
    private boolean pendingReturn = false;

    /**
     * @param callExpr Contains links to function to be evaluated ({@link org.jkcsoft.space.lang.ast.StatementBlock}s
     *                 along with local data definitions.
     * @param argTuple The Tuple of values defined in the target function parameter definition.
     */
    FunctionCallContext(Tuple ctxObject, FunctionCallExpr callExpr, Tuple argTuple) {
        super(callExpr);
        this.ctxObject = ctxObject;
        this.argTuple = argTuple;
        if (callExpr != null) {
            AbstractFunctionDefn resolvedFunctionMetaObj = callExpr.getFunctionDefnRef().getResolvedMetaObj();
            if (resolvedFunctionMetaObj instanceof FunctionDefn)
                this.blockContextStack.push(
                    new BlockContext(this, ((FunctionDefn) resolvedFunctionMetaObj).getStatementBlock()));
        }
    }

    public Tuple getCtxObject() {
        return ctxObject;
    }

    public Tuple getArgTuple() {
        return argTuple;
    }

    public BlockContext currentBlockCtxt() {
        return blockContextStack.peek();
    }

    public Assignable getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Assignable returnValue) {
        this.returnValue = returnValue;
    }

    public void setPendingReturn(boolean pendingReturn) {
        this.pendingReturn = pendingReturn;
    }

    public boolean isPendingReturn() {
        return pendingReturn;
    }

    @Override
    public String toString() {
        return "call to " + getAstNode().toString();
    }
}
