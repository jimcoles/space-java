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

import org.jkcsoft.space.lang.runtime.SpaceX;

import javax.mail.MethodNotSupportedException;
import java.util.LinkedList;
import java.util.List;

@GroupingNode
public class SpaceTypeDefnBody extends StatementBlock {

    private List<EquationDefn> equations;
    private List<TransformDefn> transformDefns;
    private List<AbstractFunctionDefn> functionDefns = new LinkedList<>();
    private StatementBlock initBlock;  // holds assignment exprs for var and assoc defns

    SpaceTypeDefnBody(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public boolean isComputed() {
        return false;
    }

    public boolean isEnumerated() {
        return true;
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
        return getAssociationDefnList().get(index);
    }

    public FunctionDefn getFunction(String name) {
        NamedElement childWithName = getChildByName(name);
        if (!(childWithName instanceof FunctionDefn))
            throw new SpaceX("reference meta object [" + childWithName + "] is not a function");

        FunctionDefn functionDefn = (FunctionDefn) childWithName;
        if (functionDefn == null) {
            throw new SpaceX("function [" + name + "] not found in " + this);
        }
        return functionDefn;
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

    public int getScalarDofs() {
        return initBlock.getScalarDofs();
    }
}
