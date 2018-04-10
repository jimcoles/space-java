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
import org.jkcsoft.space.lang.runtime.SpaceX;

/**
 * A Function is, in the abstract, a solution to an Equation.  A function
 * is a special kind of relation (Space).
 *
 * @author Jim Coles
 */
public abstract class AbstractFunctionDefn extends NamedElement implements SolutionExpr {

    private EquationDefn governingEquation; // TODO
    private SpaceTypeDefn argSpaceTypeDefn;
    private boolean isReturnVoid;
    private TypeRef returnTypeRef;

    AbstractFunctionDefn(SourceInfo sourceInfo, String name, TypeRef returnTypeRef)
    {
        super(sourceInfo, name);
        this.returnTypeRef = returnTypeRef;
        //
        addChild(this.returnTypeRef);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.FUNCTION;
    }

    public void setArgSpaceTypeDefn(SpaceTypeDefn argSpaceTypeDefn) {
        this.argSpaceTypeDefn = argSpaceTypeDefn;
        this.argSpaceTypeDefn.setGroupingNode(true);
        //
        addChild(argSpaceTypeDefn);
    }

    public SpaceTypeDefn getArgSpaceTypeDefn() {
        return argSpaceTypeDefn;
    }

    public EquationDefn getGoverningEquation() {
        return governingEquation;
    }

    public boolean isReturnVoid() {
        return isReturnVoid;
    }

    public TypeRef getReturnTypeRef() {
        if (isReturnVoid)
            throw new SpaceX("Should not call getReturnTypeRef for function with void return.");
        return returnTypeRef;
    }

}
