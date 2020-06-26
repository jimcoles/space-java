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

import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.TypeDefn;

import java.lang.reflect.Field;

/**
 * For the case of Java Field corresponding to a complex (non-simple) Java Class.
 *
 * Use direct Java field access via reflection.
 * @author Jim Coles
 */
public class SjiFieldAssocDecl extends SjiAssocDecl implements SjiFieldBased {

    // OR direct field access ...
    private Field jField;

    SjiFieldAssocDecl(SjiService sjiService, SjiTypeDefn sjiFromType, SjiTypeDefn sjiToType, Field jField) {
        super(sjiService, new NativeSourceInfo(jField), sjiFromType, sjiToType, jField.getName());
        this.jField = jField;
    }

    public boolean isProperty() {
        return false;
    }

    @Override
    public TypeDefn getType() {
        return (NumPrimitiveTypeDefn)
            getSjiService().getOrCreateSjiTypeMapping(jField.getType()).getSjiProxy();
    }

    @Override
    public Field getjField() {
        return jField;
    }
}
