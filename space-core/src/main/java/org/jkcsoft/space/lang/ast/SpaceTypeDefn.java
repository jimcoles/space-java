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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The central declarative meta notion of Space.  A
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceTypeDefn extends ModelElement {

    private SpaceTypeDefn               contextSpaceTypeDefn;
    private List<VariableDefn>          variableDefnList;
    private List<AssociationDefn>       associationDefnList;
    private List<EquationDefn>          equations;
    private List<TransformDefn>         transformDefns;
    private List<AbstractActionDefn>    functionDefns = new LinkedList<>();

    // redundant
    private Map<String, VariableDefn>       indexCoordinatesByName = new HashMap<>();
    private Map<String, AssociationDefn>    indexAssociationsByName = new HashMap<>();
    private Map<String, AbstractActionDefn> indexFunctionsByName = new HashMap<>();

    SpaceTypeDefn(String name) {
        super(name);
    }

    public boolean isComputed() {
        return false;
    }

    public boolean isEnumerated() {
        return true;
    }

    void setContextSpaceTypeDefn(SpaceTypeDefn contextSpaceTypeDefn) {
        this.contextSpaceTypeDefn = contextSpaceTypeDefn;
    }

    public SpaceTypeDefn getContextSpaceTypeDefn() {
        return contextSpaceTypeDefn;
    }

    public boolean hasContextSpaceDefn() {
        return contextSpaceTypeDefn != null;
    }

    public List<VariableDefn> getVariableDefnList() {
        return variableDefnList;
    }

    public VariableDefn addVariable(VariableDefn variableDefn) {
        if (variableDefnList == null)
            variableDefnList = new LinkedList<>();
        variableDefnList.add(variableDefn);
        //
        indexCoordinatesByName.put(variableDefn.getName(), variableDefn);
        //
        addChild(variableDefn);
        return variableDefn;
    }

    public VariableDefn getVariableDefnAt(int index) {
        return variableDefnList.get(index);
    }

    public AssociationDefn addAssociation(AssociationDefn associationDefn) {
        if (associationDefnList == null)
            associationDefnList = new LinkedList<>();
        associationDefnList.add(associationDefn);
        //
        indexAssociationsByName.put(associationDefn.getName(), associationDefn);
        //
        addChild(associationDefn);
        return associationDefn;
    }

    public AssociationDefn getAssocDefnAt(int index) {
        return associationDefnList.get(index);
    }

    public AbstractActionDefn getFunction(String name) {
        AbstractActionDefn abstractActionDefn = indexFunctionsByName.get(name);
        if (abstractActionDefn == null) {
            throw new RuntimeException("function ["+name+"] not found in " + this);
        }
        return abstractActionDefn;
    }

    public AbstractActionDefn addActionDefn(AbstractActionDefn actionDefn) {
        functionDefns.add(actionDefn);
        actionDefn.setContextSpaceTypeDefn(this);
        //
        indexFunctionsByName.put(actionDefn.getName(), actionDefn);
        //
        addChild(actionDefn);
        return actionDefn;
    }

    public String toLogString() {
        StringBuilder sb = new StringBuilder("SpaceTypeDefn {" + getName() + ": ");
        for (AbstractActionDefn functionDefn: functionDefns) {
            sb.append(functionDefn.getName()+"(),");
        }
        return sb.toString();
    }
}
