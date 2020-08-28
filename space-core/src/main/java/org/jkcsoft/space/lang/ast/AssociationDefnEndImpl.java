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

    private final int lowerMultiplicity;
    private final int upperMultiplicity;
    private final TypeRef endTypeRef;
    private final boolean isRequired;
    private final boolean isSingular;

    AssociationDefnEndImpl(SourceInfo sourceInfo, String name, TypeRef endTypeRef, boolean isRequired,
                           boolean isSingular, int lowerMultiplicity, int upperMultiplicity)
    {
        super(sourceInfo, name);
        this.endTypeRef = endTypeRef;
        this.isRequired = isRequired;
        this.isSingular = isSingular;
        this.lowerMultiplicity = lowerMultiplicity;
        this.upperMultiplicity = upperMultiplicity;
        //
        addChild(endTypeRef);
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return endTypeRef.getResolvedType();
    }


    @Override
    public int getLowerMultiplicity() {
        return lowerMultiplicity;
    }

    @Override
    public int getUpperMultiplicity() {
        return upperMultiplicity;
    }

    @Override
    public boolean isSingular() {
        return isSingular;
    }

    @Override
    public boolean isRequired() {
        return isRequired;
    }

    public TypeRef getEndTypeRef() {
        return endTypeRef;
    }
}
