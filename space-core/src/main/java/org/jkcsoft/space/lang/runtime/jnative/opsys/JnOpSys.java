/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.jnative.opsys;

import org.jkcsoft.space.lang.runtime.jnative.SpaceNative;

/**
 * @author Jim Coles
 */
@SpaceNative
public class JnOpSys {

    public void println(String msg) {
        System.out.println(msg);
    }

}
