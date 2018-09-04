/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;

import java.lang.reflect.Field;

/**
 * @author Jim Coles
 */
public class SjiFieldVarDecl extends SjiVarDecl {

    // OR direct field access ...
    private Field jField;

    SjiFieldVarDecl(SjiTypeDefn sjiTypeDefn, Field jField) {
        super(new NativeSourceInfo(jField), sjiTypeDefn, jField.getName());
        this.jField = jField;
    }

    public boolean isProperty() {
        return false;
    }

    @Override
    public NumPrimitiveTypeDefn getType() {
        return (NumPrimitiveTypeDefn)
            SpaceHome.getSjiService().getOrCreateSjiMapping(jField.getType()).getSpaceWrapper();
    }
}
