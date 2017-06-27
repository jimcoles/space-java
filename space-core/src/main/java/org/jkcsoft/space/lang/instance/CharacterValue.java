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

import org.jkcsoft.space.lang.ast.PrimitiveType;
import org.jkcsoft.space.lang.ast.VariableDefn;

/**
 * @author Jim Coles
 */
public class CharacterValue extends ScalarValue<Character> {

    /** Limit constructor access to package-only. */
    CharacterValue(PrimitiveType type, char value) {
        super(type, value);
    }

    @Override
    public String asString() {
        return null;
    }
}
