/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance.sji;

import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.instance.Value;

/**
 * @author Jim Coles
 */
public class SjiUtil {
    public static Value toSpaceValue(Object jValue) {
        Value sValue = null;
        if (jValue instanceof Integer)
            sValue = ObjectFactory.getInstance().newCardinalValue(((Integer) jValue));
        else if (jValue instanceof Float || jValue instanceof Double)
            sValue = ObjectFactory.getInstance().newRealValue(((Double) jValue));
        else if (jValue instanceof Boolean)
            sValue = ObjectFactory.getInstance().newBooleanValue(((Boolean) jValue));

        return sValue;
    }
}
