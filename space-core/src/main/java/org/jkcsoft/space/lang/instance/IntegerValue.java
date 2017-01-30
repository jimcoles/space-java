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

import org.jkcsoft.space.lang.ast.CoordinateDefn;

/**
 * @author Jim Coles
 */
public abstract class IntegerValue extends ScalarValue {

    public IntegerValue(CoordinateDefn type) {
        super(type);
    }

    @Override
    public String asString() {
        return null;
    }
}
