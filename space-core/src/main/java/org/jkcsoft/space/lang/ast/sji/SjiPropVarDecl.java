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
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.SourceInfo;

import java.beans.PropertyDescriptor;

/**
 * @author Jim Coles
 */
public class SjiPropVarDecl extends SjiVarDecl {

    private PropertyDescriptor jPropDesc;

    SjiPropVarDecl(SjiTypeDefn sjiTypeDefn, PropertyDescriptor jPd)
    {
        super(new NativeSourceInfo(jPd), sjiTypeDefn, jPd.getName());
        this.jPropDesc = jPd;
    }

    @Override
    public NumPrimitiveTypeDefn getType() {
        return (NumPrimitiveTypeDefn) SpaceHome.getSjiBuilder().getNativeType(jPropDesc.getPropertyType());
    }

    public PropertyDescriptor getjPropDesc() {
        return jPropDesc;
    }
}
