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

import org.jkcsoft.space.lang.instance.ScalarValue;
import org.jkcsoft.space.lang.instance.Tuple;

import java.util.Comparator;

/**
 * A sequenced collection of objects.
 *
 * <p> So, with Space, the conventional notion of a character string is just a
 * Object Stream where type = 'char' and length=finite.
 *
 * @author Jim Coles
 */
public class SequenceTypeDefn extends AbstractCollectionTypeDefn {

    public static final String DELIM_OPEN = "[";
    public static final String DELIM_CLOSING = "]";
    public static final String COLL_SUFFIX = DELIM_OPEN + DELIM_CLOSING;

    SequenceTypeDefn(SourceInfo sourceInfo, TypeDefn containedElementType) {
        super(sourceInfo, containedElementType.getName() + COLL_SUFFIX, containedElementType);
    }

    @Override
    public boolean isComplexType() {
        return false;
    }

    @Override
    public boolean isSetType() {
        return false;
    }

    @Override
    public boolean isSequenceType() {
        return true;
    }

    @Override
    public boolean isStreamType() {
        return false;
    }

    /** A sequence can be its own primary key as is the case for character sequences. */
    @Override
    public boolean hasPrimaryKey() {
        return false;
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        return null;
    }

    /**
     * A Sequence compares all elements
     * @return
     */
    @Override public Comparator<ScalarValue> getValueComparator() {
        return null;
    }
}
