/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
abstract public class AbstractTypeDefn extends AbstractNamedElement implements TypeDefn {

    private boolean isView;

    // The verbatim sequence of declarations and statements. Sequence is important for datum visibility rules
    // All other lists are redundantly maintained
    private List<Expression> elementSequence = new LinkedList<>();
    //
    private List<Statement> datumInits = new LinkedList<>(); // just RHS of datum decls
    //
    private List<StatementBlock> statementSequence = new LinkedList<>();  // child statements
    //
    private List<DatumDecl> datumDeclList;
    //
    private List<VariableDecl> variablesDeclList;
    private List<AssociationDefn> associationDeclList;
    private List<FunctionDefn> functionDefns = new LinkedList<>();

    //
    private SequenceTypeDefn sequenceTypeDefn;
    private SetTypeDefn setTypeDefn;

    protected AbstractTypeDefn(SourceInfo sourceInfo, NamePart namePart) {
        this(sourceInfo, namePart, false);
    }

    protected AbstractTypeDefn(SourceInfo sourceInfo, NamePart namePart, boolean isView) {
        super(sourceInfo, namePart);
        this.isView = isView;
        datumDeclList = new LinkedList<>();
        variablesDeclList = new LinkedList<>();
        associationDeclList = new LinkedList<>();
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    // ===========================================================
    // Child adders
    //

    @Override
    public DatumDeclContext addVariableDecl(VariableDecl variableDecl) {
        elementSequence.add(variableDecl);
        datumDeclList.add(variableDecl);
        variablesDeclList.add(variableDecl);
        //
        addChild(variableDecl);
        //
        return this;
    }

    @Override
    public List<VariableDecl> getVariablesDeclList() {
        return variablesDeclList;
    }

    public List<DatumDecl> getDatumDeclList() {
        return datumDeclList;
    }

    @Override
    public DatumDecl getDatum(String name) {
        return (DatumDecl) getNamedChildMap().get(name);
    }

    @Override
    public int getScalarDofs() {
        return datumDeclList.size();
    }

    @Override
    public DatumDeclContext addInitExpression(ExprStatement<AssignmentExpr> assignmentExpr) {
        elementSequence.add(assignmentExpr);
        datumInits.add(assignmentExpr);
        addChild(assignmentExpr);
        return this;
    }

    @Override
    public List<Statement> getInitializations() {
        return datumInits;
    }

    public List<StatementBlock> getStatementSequence() {
        return statementSequence;
    }

    public TypeDefn addFunctionDefn(FunctionDefn actionDefn) {
        functionDefns.add(actionDefn);
        //
        addChild(actionDefn);
        //
        return this;
    }

    public List<FunctionDefn> getFunctionDefns() {
        return functionDefns;
    }

    public FunctionDefnImpl getFunction(String name) {

        AbstractNamedElement childWithName = getChildByName(name);

        if (childWithName == null)
            throw new SpaceX("function [" + name + "] not found in " + this);

        if (!(childWithName instanceof FunctionDefnImpl))
            throw new SpaceX("reference meta object [" + childWithName + "] is not a function");

        return (FunctionDefnImpl) childWithName;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isComplex() {
        return datumDeclList.size() > 1;
    }

    @Override
    public boolean isSimple() {
        return datumDeclList.size() == 1;
    }

    @Override
    public boolean isSetType() {
        return false;
    }

    @Override
    public boolean isSequenceType() {
        return false;
    }

    @Override
    public boolean isStreamType() {
        return false;
    }

    @Override
    public boolean isView() {
        return isView;
    }

    public boolean hasDatums() {
        return datumDeclList != null && datumDeclList.size() > 0;
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        if (sequenceTypeDefn == null)
            sequenceTypeDefn = new SequenceTypeDefn(getSourceInfo(), this);
        return sequenceTypeDefn;
    }

    @Override
    public SetTypeDefn getSetOfType() {
        if (setTypeDefn == null)
            setTypeDefn = new SetTypeDefn(getSourceInfo(), this);
        return setTypeDefn;
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        return null;
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        // TODO Should compare constituent
        return receivingType == this;
    }

    public void setDerivedElements() {

    }
}
