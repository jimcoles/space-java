/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
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
public abstract class AbstractFromAssocEnd extends AbstractAssocEnd implements FromAssocEnd {

    public AbstractFromAssocEnd(SourceInfo sourceInfo, AssociationDefn assocDefn, DatumRef datumRef) {
        super(sourceInfo, assocDefn, datumRef);
    }

}
