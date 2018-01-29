/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;

/**
 * @author Jim Coles
 */
public abstract class AbstractActionDefn extends ModelElement {

    private SpaceTypeDefn contextSpaceTypeDefn;  // the calling space defn

    AbstractActionDefn(String name) {
        super(name);
    }

    public SpaceTypeDefn getContextSpaceTypeDefn() {
        return contextSpaceTypeDefn;
    }

    public AbstractActionDefn setContextSpaceTypeDefn(SpaceTypeDefn contextSpaceTypeDefn) {
        this.contextSpaceTypeDefn = contextSpaceTypeDefn;
        return this;
    }

    @Override
    public String toString() {
        return contextSpaceTypeDefn.getName() + "." + getName() + "()";
    }
}
