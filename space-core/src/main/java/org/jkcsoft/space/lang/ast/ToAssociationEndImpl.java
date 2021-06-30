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

/**
 * @author Jim Coles
 */
public class ToAssociationEndImpl extends AbstractAssocEnd implements ToAssocEnd {

    private final boolean isRequired;
    private final boolean isSingular;
    private final int lowerMultiplicity;
    private final int upperMultiplicity;

    ToAssociationEndImpl(SourceInfo sourceInfo, AssociationDefn associationDefn, DatumRef datumRef,
                         boolean isRequired, boolean isSingular, int lowerMultiplicity, int upperMultiplicity)
    {
        super(sourceInfo, associationDefn, datumRef);
        this.isRequired = isRequired;
        this.isSingular = isSingular;
        this.lowerMultiplicity = lowerMultiplicity;
        this.upperMultiplicity = upperMultiplicity;
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

}
