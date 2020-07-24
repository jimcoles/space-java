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

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;

/**
 * @author Jim Coles
 */
public class BooleanValue extends ScalarValue<BoolEnum> {

    public static final BooleanValue TRUE = new BooleanValue(NumPrimitiveTypeDefn.BOOLEAN, BoolEnum.TRUE, 1);
    public static final BooleanValue FALSE = new BooleanValue(NumPrimitiveTypeDefn.BOOLEAN, BoolEnum.FALSE, 2);
    public static final BooleanValue UNKNOWN = new BooleanValue(NumPrimitiveTypeDefn.BOOLEAN, BoolEnum.UNKNOWN, 3);

    public static final BooleanValue getValue(BoolEnum boo) {
        BooleanValue retVal = UNKNOWN;
        switch (boo) {
            case TRUE:
                retVal = TRUE;
                break;
            case FALSE:
                retVal = FALSE;
                break;
            case UNKNOWN:
            default:
        }
        return retVal;
    }

    public static final BooleanValue getValue(Boolean jBool) {
        BooleanValue retVal = UNKNOWN;
        if (jBool == true) {
            retVal = TRUE;
        }
        else if (jBool == false)
            retVal = FALSE;
        else if (jBool == null)
            retVal = UNKNOWN;

        return retVal;
    }

    private int sortValue;

    private BooleanValue(NumPrimitiveTypeDefn type, BoolEnum jValue, int sortValue) {
        super(type, jValue);
        this.sortValue = sortValue;
    }

    public int getSortValue() {
        return sortValue;
    }

    @Override
    public String asString() {
        return getJavaValue().toString();
    }
}
