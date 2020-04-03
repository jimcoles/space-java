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

import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.runtime.SpaceX;

/**
 * Holds one-dimensional values for primitive types: Ints, Real, Chars
 * @author Jim Coles
 */
public abstract class ScalarValue<T> implements Value<T> {

    private NumPrimitiveTypeDefn type;
    private T jvalue;    // the java object value such as int, float, char

    ScalarValue(NumPrimitiveTypeDefn type, T jValue) {
        if (type == null) throw new SpaceX("null type param");
        this.type = type;
        this.jvalue = jValue;
    }

    public NumPrimitiveTypeDefn getType() {
        return type;
    }

    public boolean isInit() {
        return jvalue != null;
    }

//    public void setJvalue(T jvalue) {
//        this.jvalue = jvalue;
//    }

    @Override
    public T getJavaValue() {
        return jvalue;
    }

    public abstract String asString();

    @Override
    public String toString() {
        return "("+type.getDisplayName()+") " + jvalue;
    }
}
