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

import org.jkcsoft.space.lang.ast.StatementBlock;

/**
 * @author Jim Coles
 */
public class BlockContext extends AbstractExeContext implements ExeContext {

    private FunctionCallContext functionCallContext;
    private Tuple dataTuple;

    BlockContext(FunctionCallContext functionCallContext, StatementBlock astNode, Tuple dataTuple) {
        super(astNode);
        this.functionCallContext = functionCallContext;
        this.dataTuple = dataTuple;
    }

    BlockContext(StatementBlock astNode, Tuple dataTuple) {
        this(null, astNode, dataTuple);
    }

    public boolean hasFunctionCallContext() {
        return functionCallContext != null;
    }
    public FunctionCallContext getFunctionCallContext() {
        return functionCallContext;
    }

    public Tuple getDataTuple() {
        return dataTuple;
    }
}
