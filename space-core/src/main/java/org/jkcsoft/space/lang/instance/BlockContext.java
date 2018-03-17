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
    private BlockContext previous;
    private Tuple dataTuple;

    BlockContext(FunctionCallContext functionCallContext, StatementBlock astNode) {
        super(astNode);
        this.functionCallContext = functionCallContext;
    }

    BlockContext(BlockContext previous, StatementBlock astNode) {
        super(astNode);
        this.previous = previous;
    }

}
