/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;
/**
 * @author Jim Coles
 */
public class IfStatement extends Statement {
    // if
    private BooleanExpr condition;
    // then
    private StatementBlock thenBlock;
    // else: optional else block which may contain a nested IfStatement.
    private StatementBlock elseBlock;

    public IfStatement(SourceInfo sourceInfo) {
        super(sourceInfo);
    }
}
