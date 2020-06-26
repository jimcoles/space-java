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
 * A Stream is first-class collective data type similar to the notion of a BLOB data type used by
 * databases.
 * TODO: Should we allow Space programs direct access to the raw type, or, 'Box' it with wrapper Type?
 *
 * In Space, a stream is the generalization controlling the notions of
 *
 * <ul>
 *     <li>Primitive Stream
 *     <li>Character Stream</li>
 *     <li>Object Stream</li>
 *     <li>Character Sequence</li>
 * </ul>
 * Notions to reconcile with our model of streams, sequences, strings:
 * - 'sequence/stream of keys' (references to objects) versus 'sequence/stream of marshalled objects'.
 * - Keys may be Internal (Space OID), Alternate (user-defined), or External.
 * - Internal keys are just our SpaceOID's and they are transient.
 * - 'key' might be one-byte, two-byte, int
 * - External tables may be 'well known', such as character keys.
 * - Conventional notion of 'character string' is really a 'string/sequence of well-known keys'
 *   where those keys correspond to characters/glyphs or abstract objects of abstract semantics.
 * - Space might maintain internal-to-external keys maps for efficiency of internal rep,
 *   especially if the external key is a GUID.
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
public class StreamTypeDefn extends AbstractCollectionTypeDefn implements TypeDefn {

    public static final String COLL_SUFFIX = "{}";
    private float length;

    StreamTypeDefn(SourceInfo sourceInfo, TypeDefn containedElementType) {
        super(sourceInfo, containedElementType.getName() + COLL_SUFFIX, containedElementType);
    }


    @Override
    public boolean isSetType() {
        return false;
    }

    @Override
    public boolean isSequenceType() {
        return false;
    }

    @Override
    public boolean isStreamType() {
        return true;
    }
}
