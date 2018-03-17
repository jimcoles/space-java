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
public class StatementBlock extends Statement {

    protected List<AssociationDefn> associationDefnList;
    private List<Statement> statementSequence = new LinkedList<>();  // child statements
    private List<VariableDefn> variableDefnList;

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

    public Statement addStatement(Statement statement) {
        statementSequence.add(statement);
        //
        addChild(statement);
        //
        return statement;
    }

    public boolean hasVariables() {
        return variableDefnList != null && variableDefnList.size() > 0;
    }

    public List<VariableDefn> getVariableDefnList() {
        return variableDefnList;
    }

    public VariableDefn getVariableDefnAt(int index) {
        return variableDefnList.get(index);
    }

    public boolean hasAssociations() {
        return associationDefnList != null && associationDefnList.size() > 0;
    }

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
//        indexCoordinatesByName.put(variableDefn.getName(), variableDefn);
        //
        addChild(variableDefn);
        return variableDefn;
    }

    public AssociationDefn addAssocDefn(AssociationDefn associationDefn) {
        if (associationDefnList == null)
            associationDefnList = new LinkedList<>();
        associationDefnList.add(associationDefn);
        //
//        indexAssociationsByName.put(associationDefn.getName(), associationDefn);
        //
        addChild(associationDefn);
        //

// NOTE: The following commented out because SpaceTypeDef is not the direct holder of references.
//        if (associationDefn.getFromTypeRef() != null)
//            addAssocDefn(associationDefn.getFromTypeRef());
//
//        addAssocDefn(associationDefn.getToTypeRef());
        //
        return associationDefn;
    }

    public int getScalarDofs() {
        return variableDefnList.size();
    }
}
