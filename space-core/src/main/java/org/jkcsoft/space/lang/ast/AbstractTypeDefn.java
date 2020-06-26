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
    private List<VariableDecl> variables;
    private List<AssociationDefn> associations;
    private List<ProjectionDecl> projections;
    //
    private StatementBlock initBlock;   // holds assignment exprs for var and assoc defns
    private List<FunctionDefn> functionDefns = new LinkedList<>();
    private SequenceTypeDefn sequenceTypeDefn;
    private SetTypeDefn setTypeDefn;

    protected AbstractTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
        datumDeclList = new LinkedList<>();
        variables = new LinkedList<>();
        associations = new LinkedList<>();
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
        variables.add(variableDecl);
        //
        addChild(variableDecl);
        return variableDecl;
    }

    @Override
    public AssociationDefn addAssociationDecl(AssociationDefn associationDecl) {
        datumDeclList.add(associationDecl);
        associations.add(associationDecl);
        //
        addChild(associationDecl);
        //
        return associationDecl;
    }

    @Override
    public ProjectionDecl addProjectionDecl(ProjectionDecl projectionDecl) {
        projections.add(projectionDecl);
        return projectionDecl;
    }

    public List<Declaration> getDatumDecls() {
        return datumDeclList;
    }

    @Override
    public List<VariableDecl> getVariables() {
        return variables;
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
        return false;
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
}
