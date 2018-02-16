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
 * Definition-level element of a Property.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class VariableDefn extends ModelElement {

    private PrimitiveType type;

    VariableDefn(String name, PrimitiveType type) {
        super(name);
        this.type = type;
    }

    public void setType(PrimitiveType type) {
        this.type = type;
    }

    public PrimitiveType getType() {
        return type;
    }


}
