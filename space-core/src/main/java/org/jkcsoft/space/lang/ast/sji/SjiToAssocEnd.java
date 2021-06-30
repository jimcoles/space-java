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

/**
 * @author Jim Coles
 */
public class SjiToAssocEnd extends AbstractAssocEnd implements ToAssocEnd {

    private SjiTypeDefn sjiType;

    protected SjiToAssocEnd(SourceInfo sourceInfo, SjiAssocDecl sjiAssocDecl, DatumRef datumRef)
    {
        super(sourceInfo, sjiAssocDecl, datumRef);
        this.sjiType = sjiType;
    }

    @Override
    public int getLowerMultiplicity() {
        return 0;
    }

    @Override
    public int getUpperMultiplicity() {
        return 0;
    }

    @Override
    public boolean isSingular() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
