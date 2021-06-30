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
import org.jkcsoft.space.lang.ast.FunctionDefnImpl;

import java.util.LinkedList;
import java.util.Stack;

/**
 * The instance level state for the invocation of a Function.  Might be
 * a top-level function call or nested.  Every function call is pushed onto the
 * call stack.  The FunctionCallContext objects holds data specific to this invocation.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class FunctionCallContext extends AbstractExeContext implements ExeContext {

    /** Can be > 1 context objects when processing a value expression chain. */
    private Stack<SpaceObject> ctxObjects = new Stack<>();
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
        this.ctxObjects.push(ctxObject);
        this.argTuple = argTuple;
        FunctionDefn resolvedFunctionMetaObj = (FunctionDefn) callExpr.getFunctionRef().getResolvedMetaObj();
        if (resolvedFunctionMetaObj instanceof FunctionDefnImpl) {
            BlockContext rootBlockContext =
                new BlockContext(this,
                                 ((FunctionDefnImpl) resolvedFunctionMetaObj).getStatementBlock(),
                                 ObjectFactory.getInstance().newBlockDatumMap(
                                     ((FunctionDefnImpl) resolvedFunctionMetaObj).getStatementBlock()
                                 )
                );
            addBlockContext(rootBlockContext);
        }
        this.returnValueHolder = returnValueHolder;
    }

    public void pushCtxObject(SpaceObject ctxObject) {
        ctxObjects.push(ctxObject);
    }

    public SpaceObject getCtxObject() {
        return ctxObjects.peek();
    }

    public void popCtxObject() {
        ctxObjects.pop();
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
