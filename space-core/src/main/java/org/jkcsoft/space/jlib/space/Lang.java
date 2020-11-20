/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.jlib.space;

import org.jkcsoft.space.jlib.bindings.SpaceNative;

/**
 * @author Jim Coles
 */
@SpaceNative
public class Lang {

    public String append(String s1Ref, String s2Ref) {
        return s1Ref + s2Ref;
    }

}
