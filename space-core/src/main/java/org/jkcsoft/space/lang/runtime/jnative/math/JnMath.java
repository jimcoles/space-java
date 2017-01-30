/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime.jnative.math;

import org.jkcsoft.space.lang.runtime.jnative.SpaceNative;

/**
 * @author Jim Coles
 */
@SpaceNative
public class JnMath {

    public static long add(long l1, long l2) {
        return  l1 + l2;
    }

}
