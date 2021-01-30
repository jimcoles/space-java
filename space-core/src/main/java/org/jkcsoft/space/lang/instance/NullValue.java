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
import org.jkcsoft.space.lang.ast.TypeDefn;

/**
 * @author Jim Coles
 */
public class NullValue implements Value<Object> {

//    public static final NullValue NULL_VALUE = new NullValue();

    private TypeDefn typeDefn;

    NullValue(TypeDefn typeDefn) {
        this.typeDefn = typeDefn;
    }

    @Override
    public Object getJavaValue() {
        return null;
    }

    @Override
    public boolean isNull() {
        return true;
    }

}
