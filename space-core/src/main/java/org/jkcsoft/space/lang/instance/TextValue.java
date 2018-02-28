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

import org.jkcsoft.space.lang.ast.PrimitiveType;

/**
 * TODO: In Space, a string of characters is not a scalar value.  So
 * a 'text' value should really be a reference to a CharacterSequence object.
 */
public class TextValue extends ScalarValue<String> {

    TextValue(String value) {
        super(PrimitiveType.TEXT, value);
    }

    @Override
    public String asString() {
        return getValue();
    }
}
