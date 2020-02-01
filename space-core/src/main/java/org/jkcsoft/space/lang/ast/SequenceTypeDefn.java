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

    SequenceTypeDefn(SourceInfo sourceInfo, DatumType containedElementType) {
        super(sourceInfo, containedElementType.getName() + "[]", containedElementType);
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
