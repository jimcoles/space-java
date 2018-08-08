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

import java.util.LinkedList;
import java.util.List;

@GroupingNode
@LexicalNode
public class SpaceTypeDefnBody extends StatementBlock {

    private List<EquationDefn> equations;
    private List<TransformDefn> transformDefns;
    private List<FunctionDefn> functionDefns = new LinkedList<>();
//    private StatementBlock initBlock;  // holds assignment exprs for var and assoc defns

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
        addChild((ModelElement) actionDefn);
        //
        return actionDefn;
    }


//    public StatementBlock setInitBlock(StatementBlock statementBlock) {
//        this.initBlock = statementBlock;
//        //
//        addChild(statementBlock);
//        //
//        return initBlock;
//    }
//
//    public StatementBlock getInitBlock() {
//        return initBlock;
//    }

}
