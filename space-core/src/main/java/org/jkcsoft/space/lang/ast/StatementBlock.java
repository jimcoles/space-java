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
public class StatementBlock extends AbstractModelElement implements ContextDatumDefn, Statement {

    // The verbatim sequence of declarations and statements. Sequence is important for datum visibility rules
    // All other lists are redundantly maintained
    private List<Expression> allElementsSequence = new LinkedList<>(); // declarations and statements
    // just the datum decls
    private List<Declaration> datumDecls = new LinkedList<>();
    private List<VariableDecl> varDecls = new LinkedList<>();
    private List<AssociationDefn> assocDecls = new LinkedList<>();
    // just the statements; can be any kind of statement
    private List<Statement> statementSequence = new LinkedList<>();

    public StatementBlock(SourceInfo sourceInfo) {
        this(sourceInfo, true);
    }

    /**
     * For use in creating an 'initialization' Statement Block for Type Definitions.
     * @param sourceInfo
     * @param allowDatums true for normal user-coded statement blocks; false for type init blocks.
     */
    public StatementBlock(SourceInfo sourceInfo, boolean allowDatums) {
        super(sourceInfo);
    }

    public List<Statement> getStatementSequence() {
        return statementSequence;
    }

    public Declaration addDatumDecl(Declaration declaration) {
        allElementsSequence.add(declaration);
        //
        if (declaration.isAssoc())
            assocDecls.add(((AssociationDefn) declaration));
        else
            varDecls.add((VariableDecl) declaration);
        //
        addChild(declaration);
        //
        return declaration;
    }

    public ValueExpr addValueExpr(ValueExpr valueExpr) {
        //
        allElementsSequence.add(valueExpr);
        //
        statementSequence.add(new ExprStatement<>(valueExpr));
        //
        if (valueExpr instanceof AbstractModelElement)
            addChild(((AbstractModelElement) valueExpr));
        //
        return valueExpr;
    }

    public Statement addStatement(Statement statement) {
        //
        allElementsSequence.add(statement);
        //
        statementSequence.add(statement);
        //
        addChild(statement);
        //
        return statement;
    }

    @Override
    public ContextDatumDefn addVariableDecl(VariableDecl variableDecl) {
        addDatumDecl(variableDecl);
        return this;
    }

    @Override
    public ContextDatumDefn addAssociationDecl(AssociationDefn associationDecl) {
        addDatumDecl(associationDecl);
        return this;
    }

    @Override
    public ContextDatumDefn addInitExpression(ExprStatement<AssignmentExpr> assignmentExpr) {
        addStatement(assignmentExpr);
        return this;
    }

    @Override
    public String getDisplayName() {
        return "{block}";
    }

    @Override
    public boolean hasDatums() {
        return !datumDecls.isEmpty();
    }

    @Override
    public Declaration getDatum(String name) {
        return null;
    }

    @Override
    public int getScalarDofs() {
        return datumDecls.size();
    }

    @Override
    public List<VariableDecl> getVariablesDeclList() {
        return null;
    }

    @Override
    public List<Declaration> getDatumDeclList() {
        return datumDecls;
    }
}
