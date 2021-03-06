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

import org.jkcsoft.space.lang.metameta.CharSequenceNature;

/**
 * {@link CharacterSequence} instances are the Java backing objects for
 * Space character strings. A {@link CharacterSequence} is a special kind of value
 * sequence with operations that do not apply to general sequences.
 *
 * <p>In Space, we recognize that a Character Sequence is generally an encoded
 * representation of a higher level Human-interpretable expression; therefore,
 * it is often associated with devices to marshall from or unmarshall to an in-memory
 * object graph.  In other words,
 * <p>
 *  CharacterSequence -- Unmarshaller --> Space Object Graph
 *  <br>
 *  Space Object Graph --> Marshaller --> CharacterSequence
 *
 * <p>Also, character values are physically just
 * identifiers that reference an abstract externally-defined table of 'glyphs' that
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
public class CharacterSequence extends ScalarValueSequence<CharacterValue> implements UniqueKeyStream {

    private CharSequenceNature nature = CharSequenceNature.OPAQUE;
    private String characters;
    private CharacterValue[] characterValues;

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
    public Object getJavaValue() {
        return characters;
    }

}
