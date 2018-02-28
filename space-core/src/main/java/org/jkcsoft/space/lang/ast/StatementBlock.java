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

import java.util.LinkedList;
import java.util.List;

/**
 * Applies primarily to defining a function body, or sub-block within
 * such body (including loop blocks, conditionals), or the assignments
 * associated with object initialization.
 *
 * @author Jim Coles
 */
@GroupingNode
public class StatementBlock extends Statement {

    private List<Statement> statementSequence = new LinkedList<>();  // child statements

    public StatementBlock(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public List<Statement> getStatementSequence() {
        return statementSequence;
    }

    public ValueExpr addExpr(ValueExpr valueExpr) {
        statementSequence.add(new ExprStatement<>(valueExpr));
        //
        if (valueExpr instanceof ModelElement)
            addChild(((ModelElement) valueExpr));
        //
        return valueExpr;
    }

}
