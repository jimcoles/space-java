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
public class BooleanValue extends ScalarValue<Boolean> {

    public static final BooleanValue TRUE = new BooleanValue(NumPrimitiveTypeDefn.BOOLEAN, Boolean.TRUE);
    public static final BooleanValue FALSE = new BooleanValue(NumPrimitiveTypeDefn.BOOLEAN, Boolean.FALSE);
    public static final BooleanValue getValue(boolean boo) {
        if (boo)
            return TRUE;
        else
            return FALSE;
    }

    private BooleanValue(NumPrimitiveTypeDefn type, Boolean value) {
        super(type, value);
    }

    @Override
    public String asString() {
        return Boolean.toString(getJValue());
    }
}
