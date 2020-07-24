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

import java.beans.PropertyDescriptor;

/**
 * @author Jim Coles
 */
public class SjiPropVarDecl extends SjiVarDecl implements SjiPropBased {

    private PropertyDescriptor jPropDesc;

    SjiPropVarDecl(SjiService sjiService, SjiTypeDefn sjiTypeDefn, PropertyDescriptor jPd) {
        super(sjiService, new NativeSourceInfo(jPd), sjiTypeDefn, jPd.getName());
        this.jPropDesc = jPd;
    }

    @Override
    public PropertyDescriptor getjPropDesc() {
        return jPropDesc;
    }
}
