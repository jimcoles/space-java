/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.VariableDefn;

/**
 * Holds one-dimensional values for primitive types: Ints, Real, Chars
 * @author Jim Coles
 */
public abstract class ScalarValue<T> extends SpaceObject implements Assignable {

    private VariableDefn type;
    private T value;

    ScalarValue(SpaceOid oid, VariableDefn type) {
        super(oid);
        this.type = type;
    }

    public VariableDefn getType() {
        return type;
    }

    public boolean isInit() {
        return value != null;
    }

    public T getValue() {
        return value;
    }

    public ScalarValue<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public abstract String asString();
}