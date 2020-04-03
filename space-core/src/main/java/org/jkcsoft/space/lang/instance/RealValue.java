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
public class RealValue extends ScalarValue<Double> {

    RealValue(double jValue) {
        super(NumPrimitiveTypeDefn.REAL, jValue);
    }

    @Override
    public String asString() {
        return Double.toString(getJavaValue());
    }
}
