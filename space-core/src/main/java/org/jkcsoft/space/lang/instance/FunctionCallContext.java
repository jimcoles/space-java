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

import org.jkcsoft.space.lang.ast.FunctionCallExpr;
import org.jkcsoft.space.lang.ast.FunctionDefn;
import org.jkcsoft.space.lang.ast.SpaceFunctionDefn;

import java.util.LinkedList;

/**
 * The instance level state for the invocation of a Function.  Might be
 * a top-level function call or nested.  Every function call is pushed onto the
 * call stack.  The FunctionCallContext objects holds data specific to this invocation.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class FunctionCallContext extends AbstractExeContext implements ExeContext {

    private Tuple ctxObject;
    private TupleImpl argTuple;
    private LinkedList<BlockContext> blockContexts = new LinkedList<>();
    private ValueHolder returnValueHolder;
    private boolean pendingReturn = false;

    /**
     * @param callExpr Contains links to function to be evaluated ({@link org.jkcsoft.space.lang.ast.StatementBlock}s
     *                 along with local data definitions.
     * @param argTuple The Tuple of values defined in the target function parameter definition.
     * @param returnValueHolder
     */
    FunctionCallContext(Tuple ctxObject, FunctionCallExpr callExpr, TupleImpl argTuple, ValueHolder returnValueHolder) {
        super(callExpr);
        this.ctxObject = ctxObject;
        this.argTuple = argTuple;
        FunctionDefn resolvedFunctionMetaObj = (FunctionDefn) callExpr.getFunctionRef().getResolvedMetaObj();
        if (resolvedFunctionMetaObj instanceof SpaceFunctionDefn) {
            BlockContext rootBlockContext =
                new BlockContext(this,
                                 ((SpaceFunctionDefn) resolvedFunctionMetaObj).getStatementBlock(),
                                 ObjectFactory.getInstance().newBlockDatumMap(
                                     ((SpaceFunctionDefn) resolvedFunctionMetaObj).getStatementBlock()
                                 )
                );
            addBlockContext(rootBlockContext);
        }
        this.returnValueHolder = returnValueHolder;
    }

    public Tuple getCtxObject() {
        return ctxObject;
    }

    public TupleImpl getArgTuple() {
        return argTuple;
    }

    public BlockContext getRootBlockContext() {
        return blockContexts.getFirst();
    }

    public void addBlockContext(BlockContext blockContext) {
        blockContexts.add(blockContext);
    }

    public BlockContext popBlock() {
        return blockContexts.pop();
    }

    public BlockContext currentBlockCtxt() {
        return blockContexts.getLast();
    }

    public LinkedList<BlockContext> getBlockContexts() {
        return blockContexts;
    }

    public ValueHolder getReturnValueHolder() {
        return returnValueHolder;
    }

    public void setReturnValueHolder(ValueHolder returnValueHolder) {
        this.returnValueHolder = returnValueHolder;
    }

    public boolean isPendingReturn() {
        return pendingReturn;
    }

    public void setPendingReturn(boolean pendingReturn) {
        this.pendingReturn = pendingReturn;
    }

    @Override
    public String toString() {
        return "call to " + getAstNode().toString();
    }
}
