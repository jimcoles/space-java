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

import org.jkcsoft.space.lang.ast.NamePart;
import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;

import java.lang.reflect.Field;

/**
 * Use direct Java field access via reflection.
 * @author Jim Coles
 */
public class SjiFieldVarDecl extends SjiVarDecl implements SjiFieldBased {

    // OR direct field access ...
    private Field jField;
    private NumPrimitiveTypeDefn spaceType;

    SjiFieldVarDecl(SjiService sjiService, SjiTypeDefn sjiTypeDefn, Field jField) {
        super(sjiService,
              new NativeSourceInfo(jField),
              sjiTypeDefn,
              sjiService.getAstFactory().newNamePart(new NativeSourceInfo(jField), jField.getName()));
        this.jField = jField;
    }

    public boolean isProperty() {
        return false;
    }

    public Field getjField() {
        return jField;
    }
}
