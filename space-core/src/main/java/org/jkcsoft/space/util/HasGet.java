/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.util;

/**
 * @author Jim Coles
 */
public class HasGet<T> {

    private T object;

    public HasGet(T object) {
        this.object = object;
    }

    boolean has() {
        return object != null;
    };

    T get() {
        return object;
    }
}
