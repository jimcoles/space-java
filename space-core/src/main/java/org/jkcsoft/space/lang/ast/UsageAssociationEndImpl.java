/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.ast.sji.HardReference;
import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * @author Jim Coles
 */
public class UsageAssociationEndImpl extends NamedElement implements UsageAssociationEnd {

    private HardReference<ContextDatumDefn> hardReference;

    protected UsageAssociationEndImpl(SourceInfo sourceInfo, String name, HardReference<ContextDatumDefn> hardReference) {
        super(sourceInfo, name);
        this.hardReference = hardReference;
        //
        addChild(hardReference);
    }

    public ContextDatumDefn getContextDatumDefn() {
        return hardReference.getTarget();
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

}
