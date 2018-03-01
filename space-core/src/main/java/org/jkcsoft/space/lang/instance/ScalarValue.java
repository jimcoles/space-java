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

import org.jkcsoft.space.lang.ast.PrimitiveType;
import org.jkcsoft.space.lang.ast.VariableDefn;

/**
 * Holds one-dimensional values for primitive types: Ints, Real, Chars
 * @author Jim Coles
 */
public abstract class ScalarValue<T> implements Assignable {

    private PrimitiveType type;
    private T value;

    ScalarValue(PrimitiveType type, T value) {
//        super(oid);
        this.type = type;
        this.value = value;
    }

    public PrimitiveType getType() {
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

    @Override
    public String toString() {
        return "("+type+") " + value;
    }
}
