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

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * @author Jim Coles
 */
public class SjiAssociationDefnEnd extends NamedElement implements AssociationDefnEnd {

    private SjiTypeDefn sjiType;
    private int lowerMultiplicity;
    private int upperMultiplicity;

    protected SjiAssociationDefnEnd(SourceInfo sourceInfo, String name, SjiTypeDefn sjiType, int lowerMultiplicity,
                                    int uppperMultiplicity)
    {
        super(sourceInfo, name);
        this.sjiType = sjiType;
        this.lowerMultiplicity = lowerMultiplicity;
        this.upperMultiplicity = uppperMultiplicity;
    }

    @Override
    public TypeDefn getType() {
        return sjiType;
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
        return true; // java refs are singular
    }

    @Override
    public boolean isRequired() {
        return false; // java objects are nullable (not required)
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }
}
