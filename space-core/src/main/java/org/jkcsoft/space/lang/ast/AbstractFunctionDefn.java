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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * A Function is, in the abstract, a solution to an Equation.  A function
 * is a special kind of relation (Space).
 *
 * @author Jim Coles
 */
public abstract class AbstractFunctionDefn extends NamedElement implements FunctionDefn, SolutionExpr {

    private Rule governingEquation; // TODO
    private ComplexType argSpaceTypeDefn;

    protected AbstractFunctionDefn(SourceInfo sourceInfo, String name)
    {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.FUNCTION;
    }

    public void setArgSpaceTypeDefn(ComplexType argSpaceTypeDefn) {
        this.argSpaceTypeDefn = argSpaceTypeDefn;
        this.argSpaceTypeDefn.setGroupingNode(true);
        //
        addChild(argSpaceTypeDefn);
    }

    public ComplexType getArgSpaceTypeDefn() {
        return argSpaceTypeDefn;
    }

    public Rule getGoverningEquation() {
        return governingEquation;
    }

}
