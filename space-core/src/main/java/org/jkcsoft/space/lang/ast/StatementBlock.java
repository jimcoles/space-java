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

/**
 * Applies primarily to defining a function body, or sub-block within
 * such body (including loop blocks, conditionals), or the assignments
 * associated with object initialization.
 *
 * @author Jim Coles
 */
@LexicalNode
public class StatementBlock extends Statement implements ComplexType {

//    private List<AssociationDefn> associationDefnList;
    private List<Statement> statementSequence = new LinkedList<>();  // child statements
//    private List<VariableDefn> variableDefnList;
    private List<Declaration> datumDeclList;

    public StatementBlock(SourceInfo sourceInfo) {
        super(sourceInfo);
        datumDeclList = new LinkedList<>();
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }

    @Override
    public int getScalarDofs() {
        return datumDeclList.size();
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return false;
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
    public boolean hasDatums() {
        return datumDeclList != null && datumDeclList.size() > 0;
    }


    // ===========================================================
    // Child adders
    //
    public VariableDecl addVariableDecl(VariableDeclImpl variableDecl) {
        datumDeclList.add(variableDecl);
        //
        addChild(variableDecl);
        return variableDecl;
    }

    public AssociationDecl addAssociationDecl(AssociationDeclImpl associationDecl) {
        datumDeclList.add(associationDecl);
        //
        addChild(associationDecl);
        //
        return associationDecl;
    }

    @Override
    public List<Declaration> getDatumDeclList() {
        return datumDeclList;
    }

    @Override
    public String getDisplayName() {
        return "{block}";
    }

    @Override
    public boolean isNamed() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getFullNamePath() {
        return null;
    }
}
