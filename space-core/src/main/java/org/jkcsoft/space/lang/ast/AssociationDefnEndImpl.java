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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * @author Jim Coles
 */
public class AssociationDefnEndImpl extends NamedElement implements AssociationDefnEnd {

    private int lowerMultiplicity;
    private int upperMultiplicity;
    private TypeRef endTypeRef;

    AssociationDefnEndImpl(SourceInfo sourceInfo, String name, TypeRef endTypeRef, int lowerMultiplicity,
                           int upperMultiplicity)
    {
        super(sourceInfo, name);
        this.endTypeRef = endTypeRef;
        this.lowerMultiplicity = lowerMultiplicity;
        this.upperMultiplicity = upperMultiplicity;
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return ((TypeDefn) endTypeRef.getResolvedType());
    }

    @Override
    public int getLowerMultiplicity() {
        return lowerMultiplicity;
    }

    @Override
    public int getUpperMultiplicity() {
        return upperMultiplicity;
    }

    public TypeRef getEndTypeRef() {
        return endTypeRef;
    }
}
