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
 * Let's start with a simple 'for' statement for iteration.
 * That might be all we need.
 *
 * @author Jim Coles
 */
public class ForEachStatement extends Statement {
    // for
    private VariableDefn loopVariableDefn;
    private ValueExpr spaceExpression;  // an expression that returns a Space. can be just an Assoc path.
    private StatementBlock statementBlock;

    public ForEachStatement(SourceInfo sourceInfo) {
        super(sourceInfo);
    }
}
