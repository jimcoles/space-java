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

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Applies primarily to defining a function body, or sub-block within
 * such body (including loop blocks, conditionals), or the assignments
 * associated with object initialization.
 *
 * @author Jim Coles
 */
@LexicalNode
public class StatementBlock extends AbstractTypeDefn implements Statement, TypeDefn {

//    private List<AssociationDefn> associationDefnList;
    private List<Statement> statementSequence = new LinkedList<>();  // child statements

    public StatementBlock(SourceInfo sourceInfo) {
        super(sourceInfo, "(block context)");
    }

    public List<Statement> getStatementSequence() {
        return statementSequence;
    }

    public ValueExpr addExpr(ValueExpr valueExpr) {
        statementSequence.add(new ExprStatement<>(valueExpr));
        //
        if (valueExpr instanceof AbstractModelElement)
            addChild(((AbstractModelElement) valueExpr));
        //
        return valueExpr;
    }

    public Statement addStatement(Statement statement) {
        statementSequence.add(statement);
        //
        addChild(statement);
        //
        return statement;
    }

    @Override
    public String getDisplayName() {
        return "{block}";
    }

    @Override
    public KeyDefn getPrimaryKeyDefn() {
        return null;
    }

    @Override
    public Set<KeyDefn> getAllKeyDefns() {
        return null;
    }
}
