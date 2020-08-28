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
abstract public class AbstractTypeDefn extends NamedElement implements TypeDefn {

    private boolean isView = false;

    private List<Declaration> datumDeclList;
    //
    private List<VariableDecl> variablesDeclList;
    private List<AssociationDefn> associationDeclList;
    private List<ProjectionDecl> projectionDeclList;
    //
    private StatementBlock initBlock;   // holds assignment exprs for var and assoc defns
    private List<FunctionDefn> functionDefns = new LinkedList<>();
    private SequenceTypeDefn sequenceTypeDefn;
    private SetTypeDefn setTypeDefn;

    protected AbstractTypeDefn(SourceInfo sourceInfo, String name) {
        this(sourceInfo, name, false);
    }

    protected AbstractTypeDefn(SourceInfo sourceInfo, String name, boolean isView) {
        super(sourceInfo, name);
        this.isView = isView;
        datumDeclList = new LinkedList<>();
        variablesDeclList = new LinkedList<>();
        associationDeclList = new LinkedList<>();
        projectionDeclList = new LinkedList<>();
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    // ===========================================================
    // Child adders
    //

    @Override
    public VariableDecl addVariableDecl(VariableDecl variableDecl) {
        datumDeclList.add(variableDecl);
        variablesDeclList.add(variableDecl);
        //
        addChild(variableDecl);
        return variableDecl;
    }

    @Override
    public List<VariableDecl> getVariablesDeclList() {
        return variablesDeclList;
    }

    @Override
    public AssociationDefn addAssociationDecl(AssociationDefn associationDecl) {
        datumDeclList.add(associationDecl);
        associationDeclList.add(associationDecl);
        //
        addChild(associationDecl);
        //
        return associationDecl;
    }

    @Override
    public ProjectionDecl addProjectionDecl(ProjectionDecl projectionDecl) {
        projectionDeclList.add(projectionDecl);
        return projectionDecl;
    }

    @Override
    public List<ProjectionDecl> getProjectionDeclList() {
        return projectionDeclList;
    }

    public List<Declaration> getDatumDeclList() {
        return datumDeclList;
    }

    @Override
    public Declaration getDatum(String name) {
        return (Declaration) getNamedChildMap().get(name);
    }

    @Override
    public int getScalarDofs() {
        return datumDeclList.size();
    }

    @Override
    public StatementBlock getInitBlock() {
        return initBlock;
    }

    public List<FunctionDefn> getFunctionDefns() {
        return functionDefns;
    }

    public SpaceFunctionDefn getFunction(String name) {
        NamedElement childWithName = getChildByName(name);
        if (!(childWithName instanceof SpaceFunctionDefn))
            throw new SpaceX("reference meta object [" + childWithName + "] is not a function");

        SpaceFunctionDefn functionDefn = (SpaceFunctionDefn) childWithName;
        if (functionDefn == null) {
            throw new SpaceX("function [" + name + "] not found in " + this);
        }
        return functionDefn;
    }

    public FunctionDefn addFunctionDefn(FunctionDefn actionDefn) {
        functionDefns.add(actionDefn);
        //
        addChild((AbstractModelElement) actionDefn);
        //
        return actionDefn;
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isComplexType() {
        return datumDeclList.size() > 1;
    }

    @Override
    public boolean isSimpleType() {
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
    public boolean isAssignableTo(TypeDefn receivingType) {
        // TODO Should compare constituent
        return receivingType == this;
    }

    public void setDerivedElements() {

    }
}
