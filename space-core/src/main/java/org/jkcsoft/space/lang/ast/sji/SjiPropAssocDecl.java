/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.lang.ast.Comparators;
import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.runtime.SpaceUtils;

import java.beans.PropertyDescriptor;

/**
 * @author Jim Coles
 */
public class SjiPropAssocDecl extends SjiAssocDecl implements SjiPropBased {

    private PropertyDescriptor jPropDesc;

    public SjiPropAssocDecl(SjiService sjiService, SjiTypeDefn sjiFromType, SjiTypeDefn sjiToType, PropertyDescriptor jPd) {
        super(sjiService, new NativeSourceInfo(jPd), sjiFromType, sjiToType, jPd.getName());
        this.jPropDesc = jPd;
    }

    @Override
    public PropertyDescriptor getjPropDesc() {
        return jPropDesc;
    }

    @Override
    public Comparators.DatumTupleComparator getDatumComparator() {
        throw SpaceUtils.nosup("getDatumComparator");
    }
}
