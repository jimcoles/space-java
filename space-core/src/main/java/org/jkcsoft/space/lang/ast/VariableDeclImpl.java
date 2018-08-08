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
 * Meta-level element of a Variable. Declares a named 'usage' of
 * a Primitive by a Type.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class VariableDeclImpl extends NamedElement implements VariableDecl {

    private NumPrimitiveTypeDefn type;

    VariableDeclImpl(SourceInfo sourceInfo, String name, NumPrimitiveTypeDefn type) {
        super(sourceInfo, name);
        this.type = type;
        //
        addChild(type);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public NumPrimitiveTypeDefn getType() {
        return type;
    }

    public void setType(NumPrimitiveTypeDefn type) {
        this.type = type;
    }

}
