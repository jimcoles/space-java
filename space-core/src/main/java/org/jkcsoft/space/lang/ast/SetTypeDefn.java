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

import java.util.List;

/**
 * Just a wrapper around the {@link SpaceTypeDefn} associated with this set's contents,
 * but distinguishes 'set of type' versus just 'type' for type checking and execution
 * purposes.
 *
 * @author Jim Coles
 */
public class SetTypeDefn extends AbstractCollectionTypeDefn {

    public static final String COLL_SUFFIX = "{}";

    SetTypeDefn(SourceInfo sourceInfo, DatumType containedElementType) {
        super(sourceInfo, containedElementType.getName() + COLL_SUFFIX, containedElementType);
    }

}
