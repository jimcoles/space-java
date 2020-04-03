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

import org.jkcsoft.space.lang.runtime.SpaceX;

import java.util.LinkedList;
import java.util.List;

/**
 * The base class for some very important sub-types.
 *
 * @author Jim Coles
 */
public abstract class AbstractProjection extends AbstractDatumTypeDefn implements Projection {

    private List<Declaration> datumDeclList;
    private List<VariableDecl> variables;
    private List<AssociationDefn> associations;
    private StatementBlock initBlock;   // holds assignment exprs for var and assoc defns
    private List<FunctionDefn> functionDefns = new LinkedList<>();

    protected AbstractProjection(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
        datumDeclList = new LinkedList<>();
    }

    // ===========================================================
    // Child adders
    //
    @Override
    public VariableDecl addVariableDecl(VariableDecl variableDecl) {
        datumDeclList.add(variableDecl);
        //
        addChild(variableDecl);
        return variableDecl;
    }

    @Override
    public AssociationDefn addAssociationDecl(AssociationDefn associationDecl) {
        datumDeclList.add(associationDecl);
        //
        addChild(associationDecl);
        //
        return associationDecl;
    }

    public List<Declaration> getDatumDeclList() {
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
    public boolean isPrimitiveType() {
        return false;
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
    public boolean isAssignableTo(DatumType argsType) {
        return false;
    }

    @Override
    public boolean isSimpleType() {
        return false;
    }

    @Override
    public boolean isComplexType() {
        return true;
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

    public boolean hasDatums() {
        return datumDeclList != null && datumDeclList.size() > 0;
    }
}
