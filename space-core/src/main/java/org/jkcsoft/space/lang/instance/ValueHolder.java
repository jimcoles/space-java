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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;

/**
 * Variables and References hold values.
 *
 * @author Jim Coles
 */
public interface ValueHolder<V extends Value> {

    Declaration getDeclaration();

    default DatumType getType() {
        return getDeclaration().getType();
    }

    /** Assign @value to this holder slot. */
    void setValue(V value);

    /** Does this value slot have an assignable value? */
    boolean hasValue();

    /**
     * Get the assignable value that the Executor will use move to a left-hand side of
     * assignment statement or other assignment semantics.
     * For scalars this a primitive value such as a integer.
     * For {@link org.jkcsoft.space.lang.ast.AssociationDefn}, this is the SpaceOid.
     * For Java Objects, this is the actual Java Object.
     */
    V getValue();

}
