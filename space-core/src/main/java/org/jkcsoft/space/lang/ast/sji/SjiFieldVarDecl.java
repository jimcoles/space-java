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
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.lang.reflect.Field;

/**
 * @author Jim Coles
 */
public class SjiFieldVarDecl extends SjiVarDecl {

    // OR direct field access ...
    private Field jField;

    SjiFieldVarDecl(SourceInfo sourceInfo, SjiTypeDefn sjiTypeDefn, Field jField) {
        super(sourceInfo, sjiTypeDefn, jField.getName());
        this.jField = jField;
    }

    public boolean isProperty() {
        return false;
    }

    @Override
    public NumPrimitiveTypeDefn getType() {
        return (NumPrimitiveTypeDefn)
            SpaceHome.getSjiBuilder().getSjiMapping(jField.getType()).getSpaceTypeRef().getResolvedMetaObj();
    }
}
