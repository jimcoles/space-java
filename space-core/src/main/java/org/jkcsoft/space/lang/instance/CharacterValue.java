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

import org.jkcsoft.space.lang.ast.PrimitiveTypeDefn;

/**
 * @author Jim Coles
 */
public class CharacterValue extends ScalarValue<Character> {

    /** Limit constructor access to package-only. */
    CharacterValue(PrimitiveTypeDefn type, char value) {
        super(type, value);
    }

    @Override
    public String asString() {
        return String.valueOf(getValue());
    }
}
