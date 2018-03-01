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

import org.jkcsoft.java.util.Strings;

import java.util.LinkedList;
import java.util.List;

/**
 * The central declarative, data-centric, meta notion of Space.
 * <p>
 * <p>
 * OOP Analog: Class (definition) RDB Analog: Table definition (creat table ... statement)
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceTypeDefn extends NamedElement {

    private boolean isEntity;
    private SpaceTypeDefnBody body;

    SpaceTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    public List<TransformDefn> getTransformDefns() {
        return body.getTransformDefns();
    }

    public List<AbstractFunctionDefn> getFunctionDefns() {
        return body.getFunctionDefns();
    }

    public boolean isComputed() {
        return body.isComputed();
    }

    public boolean isEnumerated() {
        return body.isEnumerated();
    }

    public List<VariableDefn> getVariableDefnList() {
        return body.getVariableDefnList();
    }

    public VariableDefn getVariableDefnAt(int index) {
        return body.getVariableDefnAt(index);
    }

    public AssociationDefn getAssocDefnAt(int index) {
        return body.getAssocDefnAt(index);
    }

    public FunctionDefn getFunction(String name) {
        return body.getFunction(name);
    }

    public VariableDefn addVariable(VariableDefn variableDefn) {
        return body.addVariable(variableDefn);
    }

    public AssociationDefn addAssocDefn(AssociationDefn associationDefn) {
        return body.addAssocDefn(associationDefn);
    }

    public AbstractFunctionDefn addFunctionDefn(AbstractFunctionDefn functionDefn) {
        return body.addFunctionDefn(functionDefn);
    }

    public boolean hasVariables() {
        return body.hasVariables();
    }

    public boolean hasAssociations() {
        return body.hasAssociations();
    }

    public List<AssociationDefn> getAssociationDefnList() {
        return body.getAssociationDefnList();
    }

    /**
     * Might not be needed; essentially, asking 'is this a basis type' or a 'derived' type.
     */
    public boolean isEntity() {
        return isEntity;
    }

    public SpaceTypeDefnBody setBody(SpaceTypeDefnBody body) {
        this.body = body;
        //
        addChild(body);
        //
        return body;
    }

    public SpaceTypeDefnBody getBody() {
        return body;
    }


    public String toLogString() {
        StringBuilder sb = new StringBuilder("SpaceTypeDefn {" + getName() + ": ");
        Strings.buildCommaDelList(body.getVariableDefnList());
        return sb.toString();
    }

    public boolean hasMembers() {
        return body.hasVariables() || body.hasAssociations();
    }

    public List<NamedElement> getAllMembers() {
        LinkedList<NamedElement> namedElements = new LinkedList<>();
        if (hasVariables())
            namedElements.addAll(getVariableDefnList());
        if (hasAssociations())
            namedElements.addAll(getAssociationDefnList());
        return namedElements;
    }

}
