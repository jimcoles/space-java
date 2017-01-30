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

    private SpaceDefn   contextSpaceDefn;  // the calling space defn

    public AbstractActionDefn(SpaceDefn contextSpaceDefn, String name) {
        super(name);
        this.contextSpaceDefn = contextSpaceDefn;
    }

    public SpaceDefn getContextSpaceDefn() {
        return contextSpaceDefn;
    }

    public AbstractActionDefn setContextSpaceDefn(SpaceDefn contextSpaceDefn) {
        this.contextSpaceDefn = contextSpaceDefn;
        return this;
    }

    @Override
    public String toString() {
        return contextSpaceDefn.getName() + "." + getName() + "()";
    }
}
