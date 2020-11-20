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
    private BlockDatumMap blockDatumMap;

    BlockContext(FunctionCallContext functionCallContext, StatementBlock astNode, BlockDatumMap blockDatumMap) {
        super(astNode);
        this.functionCallContext = functionCallContext;
        this.blockDatumMap = blockDatumMap;
    }

    BlockContext(StatementBlock astNode, BlockDatumMap blockDatumMap) {
        this(null, astNode, blockDatumMap);
    }

    public boolean hasFunctionCallContext() {
        return functionCallContext != null;
    }
    public FunctionCallContext getFunctionCallContext() {
        return functionCallContext;
    }

    public BlockDatumMap getBlockDatumMap() {
        return blockDatumMap;
    }
}
