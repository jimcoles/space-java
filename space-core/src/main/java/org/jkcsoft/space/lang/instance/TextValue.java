package org.jkcsoft.space.lang.instance;/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

import org.jkcsoft.space.lang.ast.PrimitiveType;

public class TextValue extends ScalarValue<String> {

    TextValue(String value) {
        super(PrimitiveType.TEXT, value);
    }

    @Override
    public String asString() {
        return getValue();
    }
}
