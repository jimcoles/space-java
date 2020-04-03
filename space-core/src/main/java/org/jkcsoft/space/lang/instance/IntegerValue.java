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
 * @author Jim Coles
 */
public abstract class IntegerValue extends ScalarValue<Long> {

    public IntegerValue(NumPrimitiveTypeDefn primitiveTypeDefn, Long value) {
        super(primitiveTypeDefn, value);
    }

    @Override
    public String asString() {
        return Long.toString(getJavaValue());
    }
}
