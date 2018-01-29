/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;

import java.lang.reflect.Method;

/**
 * @author Jim Coles
 */
public class NativeActionDefn extends AbstractActionDefn implements Callable {

    private final Method jMethod;
    private final SpaceTypeDefn argSpaceTypeDefn;

    NativeActionDefn(String name, Method jMethod, SpaceTypeDefn argSpaceTypeDefn) {
        super(name);
        this.jMethod = jMethod;
        this.argSpaceTypeDefn = argSpaceTypeDefn;
    }

    public Method getjMethod() {
        return jMethod;
    }

    @Override
    public SpaceTypeDefn getArgSpaceTypeDefn() {
        return argSpaceTypeDefn;
    }
}
