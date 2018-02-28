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

/**
 * A Function is, in the abstract, a solution to an Equation.  A function
 * is a special kind of relation (Space).
 *
 * @author Jim Coles
 */
public abstract class AbstractFunctionDefn extends NamedElement {

    private EquationDefn governingEquation;
    private SpaceTypeDefn contextSpaceTypeDefn;  // the calling space defn
    private SpaceTypeDefn argSpaceTypeDefn;

    AbstractFunctionDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    public SpaceTypeDefn getContextSpaceTypeDefn() {
        return contextSpaceTypeDefn;
    }

    public AbstractFunctionDefn setContextSpaceTypeDefn(SpaceTypeDefn contextSpaceTypeDefn) {
        this.contextSpaceTypeDefn = contextSpaceTypeDefn;
        return this;
    }

    public String toLogString() {
        return contextSpaceTypeDefn.getName() + "." + getName() + "()";
    }

    public void setArgSpaceTypeDefn(SpaceTypeDefn argSpaceTypeDefn) {
        this.argSpaceTypeDefn = argSpaceTypeDefn;
        argSpaceTypeDefn.setParent(this.getContextSpaceTypeDefn());
    }

    public SpaceTypeDefn getArgSpaceTypeDefn() {
        return argSpaceTypeDefn;
    }
}
