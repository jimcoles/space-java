/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;

/**
 * @author Jim Coles
 */
public class DetachedHolder<T extends Value> implements ValueHolder<T> {

    private DatumType type;
    T value;

    @Override
    public Declaration getDeclaration() {
        return null;
    }

    @Override
    public DatumType getType() {
        return type;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public T getValue() {
        return value;
    }
}
