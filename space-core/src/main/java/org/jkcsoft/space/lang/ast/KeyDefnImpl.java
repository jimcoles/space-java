/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.LinkedList;
import java.util.List;

/**
 * We're extending {@link ViewDefnImpl} because a Key is just a list of variables.
 *
 * @author Jim Coles
 */
public class KeyDefnImpl extends ViewDefnImpl implements KeyDefn {

    protected KeyDefnImpl(SourceInfo sourceInfo, NamePart namePart, TypeDefn basisTypeDefn) {
        super(sourceInfo, namePart, basisTypeDefn);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

}
