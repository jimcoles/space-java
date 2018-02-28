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

import org.jkcsoft.space.lang.runtime.RuntimeException;

import java.util.LinkedList;
import java.util.List;

@GroupingNode
public class SpaceTypeDefnBody extends ModelElement {

    private List<VariableDefn> variableDefnList;
    private List<AssociationDefn> associationDefnList;
    private List<EquationDefn> equations;
    private List<TransformDefn> transformDefns;
    private List<AbstractFunctionDefn> functionDefns = new LinkedList<>();
    private StatementBlock initBlock;

    // redundant
//    private Map<String, VariableDefn>       indexCoordinatesByName = new HashMap<>();
//    private Map<String, AssociationDefn>    indexAssociationsByName = new HashMap<>();
//    private Map<String, AbstractFunctionDefn> indexFunctionsByName = new HashMap<>();

    SpaceTypeDefnBody(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public boolean isComputed() {
        return false;
    }

    public boolean isEnumerated() {
        return true;
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

    public List<EquationDefn> getEquations() {
        return equations;
    }

    public List<TransformDefn> getTransformDefns() {
        return transformDefns;
    }

    public List<AbstractFunctionDefn> getFunctionDefns() {
        return functionDefns;
    }

    public AssociationDefn getAssocDefnAt(int index) {
        return associationDefnList.get(index);
    }

    public FunctionDefn getFunction(String name) {
        NamedElement childWithName = getChildByName(name);
        if (!(childWithName instanceof FunctionDefn))
            throw new RuntimeException("reference meta object [" + childWithName + "] is not a function");

        FunctionDefn functionDefn = (FunctionDefn) childWithName;
        if (functionDefn == null) {
            throw new RuntimeException("function [" + name + "] not found in " + this);
        }
        return functionDefn;
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

    public AbstractFunctionDefn addFunctionDefn(AbstractFunctionDefn actionDefn) {
        functionDefns.add(actionDefn);
        //
        addChild(actionDefn);
        //
        return actionDefn;
    }

    public StatementBlock setInitBlock(StatementBlock statementBlock) {
        this.initBlock = statementBlock;
        //
        addChild(statementBlock);
        //
        return initBlock;
    }

    public StatementBlock getInitBlock() {
        return initBlock;
    }
}
