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

import org.jkcsoft.space.lang.ast.PrimitiveTypeDefn;

/**
 * Holds one-dimensional values for primitive types: Ints, Real, Chars
 * @author Jim Coles
 */
public abstract class ScalarValue<T> implements Assignable {

    private PrimitiveTypeDefn type;
    private T jvalue;    // the java object value such as int, float, char

    ScalarValue(PrimitiveTypeDefn type, T jValue) {
        this.type = type;
        this.jvalue = jValue;
    }

    public PrimitiveTypeDefn getType() {
        return type;
    }

    public boolean isInit() {
        return jvalue != null;
    }

    public T getJvalue() {
        return jvalue;
    }

    public ScalarValue<T> setJvalue(T jvalue) {
        this.jvalue = jvalue;
        return this;
    }

    public abstract String asString();

    @Override
    public String toString() {
        return "ScalarValue("+type.getDisplayName()+") " + jvalue;
    }
}
