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

import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;

/**
 * Big plans for the Space model characters and character sets, etc.
 * Basically we need to lay a Space model over the Java model to enable
 * Space queries and views of character sets. Should tie into the Unicode
 * model (http://www.unicode.org/versions/Unicode13.0.0/).
 *
 * @author Jim Coles
 */
public class CharacterValue extends ScalarValue<Character> {

    CharacterValue(char value) {
        super(NumPrimitiveTypeDefn.CHAR, value);
    }

    @Override
    public String asString() {
        return String.valueOf(getJavaValue());
    }

}
