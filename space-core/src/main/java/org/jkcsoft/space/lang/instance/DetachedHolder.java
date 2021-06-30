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

import org.jkcsoft.space.lang.ast.DatumDecl;
import org.jkcsoft.space.lang.ast.TypeDefn;

/**
 * A {@link ValueHolder} for literals or expression eval results before being assigned to
 * declared datums. A {@link DetachedHolder} has a type but not a name (anonymous).
 *
 * @author Jim Coles
 */
public class DetachedHolder<V extends Value<J>, J> implements ValueHolder<V, J> {

    private TypeDefn type;
    private V value;

    public DetachedHolder(TypeDefn type, V value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public DatumDecl getDeclaration() {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return type;
    }

    @Override
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Detached Holder: " + "(" + type + ")(anon)=" + value;
    }
}
