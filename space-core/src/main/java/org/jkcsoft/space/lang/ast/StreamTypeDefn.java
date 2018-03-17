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

/**
 * In Space, a stream is the generalization controling the notions of
 *
 * <ul>
 *     <li>Object Stream</li>
 *     <li>Primitive Stream
 *     <li>Character Stream</li>
 *     <li>Character Sequence</li>
 * </ul>
 *
 * So, an Object Stream is an indefinite sequence of multi-dimensional objects
 * whereas a Character Sequence is a finite sequence of 1-dimensional primitives.
 * And, these are all just views of each other.  For examples, an Object Stream
 * may always be 'viewed' as a Byte Sequence.
 *
 * <p> So, with Space, the conventional notion of a character string is just a
 * Object Stream where type = 'char' and length=finite.
 *
 * @author Jim Coles
 */
public class StreamTypeDefn extends NamedElement implements DatumType {

    private MetaReference<SpaceTypeDefn> typeRef;
    private float length;

    StreamTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    @Override
    public int getScalarDofs() {
        return 0;
    }
}
