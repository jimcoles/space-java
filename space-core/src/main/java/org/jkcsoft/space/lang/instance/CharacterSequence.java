/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.metameta.CharSequenceNature;

/**
 * Roughly equivalent to a character string in other languages.  In Space,
 * we recognize that a Character Sequence is always some encoded
 * representation of a higher level expression; therefore, it is often
 * associated with devices to marshall from or unmarshall to an in-memory
 * representation.
 *
 * @author Jim Coles
 */
public class CharacterSequence extends BinarySequence {

    private CharSequenceNature nature = CharSequenceNature.OPAQUE;
    private String characters;
    private CharacterValue[] characterValues;

    /** Limit constructor access to package-only. */
    CharacterSequence(SpaceOid oid, CharacterValue[] characterValues) {
        super(oid);
        this.characterValues = characterValues;
    }

    public CharacterSequence(SpaceOid oid, String characters) {
        super(oid);
        this.characters = characters;
    }

    public CharSequenceNature getNature() {
        return nature;
    }

    public CharacterValue[] getCharacters() {
        return characterValues;
    }

    @Override
    public String toString() {
        return characters;
    }
}
