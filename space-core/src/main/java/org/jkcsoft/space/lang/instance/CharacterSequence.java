/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.PrimitiveTypeDefn;
import org.jkcsoft.space.lang.metameta.CharSequenceNature;

/**
 * Roughly equivalent to a character string in other languages.  In Space,
 * we recognize that a Character Sequence is generally some encoded
 * representation of a higher level expression; therefore, it is often
 * associated with devices to marshall from or unmarshall to an in-memory
 * representation.  In other words,
 * <p>
 *  CharacterSequence -- Unmarshaller --> Space
 *  <br>
 *  Space --> Marshaller --> CharacterSequence
 * <p>Also, since the values of a character are 'physically' just
 * identifiers that reference an abstract table of 'glyphs' that
 * represent a character. E.g.:</p>
 * "abcd" -> "id(a)id(b)id(c)id(d)"
 *
 * Table for UTF-16 character set:
 * <table border="1px">
 *     <th>id</th>
 * </table>
 *
 * <p>Mapping of one character set to another:</p>
 * <table border="2px">
 *      <th>My Charset ID</th><th>Std Charset ID</th>
 *      <tr><td>01</td><td>07</td></tr>
 * </table>
 *
 * @author Jim Coles
 */
public class CharacterSequence extends BinarySequence<CharacterValue> implements UniqueKeyStream {

    private CharSequenceNature nature = CharSequenceNature.OPAQUE;
    private String characters;
    private CharacterValue[] characterValues;

    /** Limit constructor access to package-only. */
    CharacterSequence(SpaceOid oid, CharacterValue[] characterValues) {
        super(oid, null);
        this.characterValues = characterValues;
    }

    CharacterSequence(SpaceOid oid, String characters) {
        super(oid, null);
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

    @Override
    public CharacterValue getElement(int index) {
        return characterValues[index];
    }

    @Override
    public DatumType getType() {
        return NumPrimitiveTypeDefn.CHAR.getSequenceOfType();
    }

    @Override
    public Object getJValue() {
        return characters;
    }
}
