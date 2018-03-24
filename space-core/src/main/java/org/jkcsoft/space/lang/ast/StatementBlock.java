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
@LexicalNode
public class StatementBlock extends Statement implements TupleDefn {

    private List<AssociationDefn> associationDefnList;
    private List<Statement> statementSequence = new LinkedList<>();  // child statements
    private List<VariableDefn> variableDefnList;

    public StatementBlock(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public int getScalarDofs() {
        return variableDefnList.size();
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

    public Statement addStatement(Statement statement) {
        statementSequence.add(statement);
        //
        addChild(statement);
        //
        return statement;
    }

    @Override
    public boolean hasVariables() {
        return variableDefnList != null && variableDefnList.size() > 0;
    }

    @Override
    public List<VariableDefn> getVariableDefnList() {
        return variableDefnList;
    }

    public VariableDefn getVariableDefnAt(int index) {
        return variableDefnList.get(index);
    }

    @Override
    public boolean hasAssociations() {
        return associationDefnList != null && associationDefnList.size() > 0;
    }

    @Override
    public List<AssociationDefn> getAssociationDefnList() {
        return associationDefnList;
    }

    // ===========================================================
    // Child adders
    //
    public VariableDefn addVariable(VariableDefn variableDefn) {
        if (variableDefnList == null)
            variableDefnList = new LinkedList<>();
        variableDefnList.add(variableDefn);
        //
        addChild(variableDefn);
        return variableDefn;
    }

    public AssociationDefn addAssocDefn(AssociationDefn associationDefn) {
        if (associationDefnList == null)
            associationDefnList = new LinkedList<>();
        associationDefnList.add(associationDefn);
        //
        addChild(associationDefn);
        //
        return associationDefn;
    }

    @Override
    public List<NamedElement> getAllMembers() {
        LinkedList<NamedElement> namedElements = new LinkedList<>();
        if (hasVariables())
            namedElements.addAll(getVariableDefnList());
        if (hasAssociations())
            namedElements.addAll(getAssociationDefnList());
        return namedElements;
    }

    @Override
    public String getDisplayName() {
        return "{block}";
    }

}
