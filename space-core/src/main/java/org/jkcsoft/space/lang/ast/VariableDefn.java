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
 * Definition-level element of a Property.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class VariableDefn extends NamedElement {

    private PrimitiveTypeDefn type;

    VariableDefn(SourceInfo sourceInfo, String name, PrimitiveTypeDefn type) {
        super(sourceInfo, name);
        this.type = type;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public PrimitiveTypeDefn getType() {
        return type;
    }

    public void setType(PrimitiveTypeDefn type) {
        this.type = type;
    }

}
