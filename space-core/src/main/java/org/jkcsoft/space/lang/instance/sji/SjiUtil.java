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

import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.runtime.SpaceX;

/**
 * @author Jim Coles
 */
public class SjiUtil {

    private static SjiService sji = SpaceHome.getSjiService();

    public static Value toSpaceValue(Object jValue) {
        Value sValue = null;
        ObjectFactory spaceInst = ObjectFactory.getInstance();
        if (jValue instanceof Integer || jValue instanceof Long)
            sValue = spaceInst.newCardinalValue(((Long) jValue));
        else if (jValue instanceof Float || jValue instanceof Double)
            sValue = spaceInst.newRealValue(((Double) jValue));
        else if (jValue instanceof Boolean)
            sValue = spaceInst.newBooleanValue(((Boolean) jValue));
        else if (jValue instanceof String)
            sValue = spaceInst.newCharacterSequence((String) jValue);
        else
            // TODO Handle Java values that are not Java primitives or String
//            sValue = sji.
            throw new SpaceX("don't know how to convert Java value to Space value: [{}]", jValue);

        return sValue;
    }

}
