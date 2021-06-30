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
public class SjiFromAssocEnd extends AbstractFromAssocEnd {

    private SjiTypeDefn sjiType;

    protected SjiFromAssocEnd(SourceInfo sourceInfo, SjiAssocDecl sjiAssocDecl, DatumRef datumRef)
    {
        super(sourceInfo, sjiAssocDecl, datumRef);
        this.sjiType = sjiType;
    }

}
