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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.NullType;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.VoidType;

/**
 * @author Jim Coles
 */
public class NullValue extends ScalarValue {

    public static final NullValue NULL_VALUE = new NullValue();

    private NullValue() {
        super(NumPrimitiveTypeDefn.NULL, null);
    }

    @Override
    public String asString() {
        return null;
    }

}
