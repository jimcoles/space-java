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

import org.jkcsoft.space.lang.runtime.SpaceUtils;

/**
 * Just a wrapper around the {@link TypeDefnImpl} associated with this set's contents,
 * but distinguishes 'set of type' versus just 'type' for type checking and execution
 * purposes.
 *
 * @author Jim Coles
 */
public class SetTypeDefn extends AbstractCollectionTypeDefn {

    public static final String COLL_SUFFIX = "{}";

    SetTypeDefn(SourceInfo sourceInfo, TypeDefn containedElementType) {
        super(sourceInfo,
              new NamePart(SourceInfo.INTRINSIC, containedElementType.getName() + COLL_SUFFIX),
              containedElementType);
    }

    @Override
    public boolean isComplex() {
        return false;
    }

    @Override
    public boolean isSetType() {
        return true;
    }

    @Override
    public boolean isSequenceType() {
        return false;
    }

    @Override
    public boolean isStreamType() {
        return false;
    }

    @Override
    public boolean hasPrimaryKey() {
        return false;
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        throw SpaceUtils.nosup("getTypeComparator");
    }
}
