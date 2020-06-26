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
}
