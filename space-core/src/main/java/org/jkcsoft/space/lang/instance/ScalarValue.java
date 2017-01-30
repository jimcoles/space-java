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
 * Holds one-dimensional values for primitive types: Ints, Floats, Chars
 * @author Jim Coles
 */
public abstract class ScalarValue {

    private CoordinateDefn type;

    public ScalarValue(CoordinateDefn type) {
        this.type = type;
    }

    public CoordinateDefn getType() {
        return type;
    }

    public abstract String asString();
}
