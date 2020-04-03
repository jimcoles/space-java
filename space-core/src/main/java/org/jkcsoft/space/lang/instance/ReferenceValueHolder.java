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
public interface ReferenceValueHolder<J, V extends ReferenceValue<J>> extends ValueHolder<V> {

    /** Assign @value to this holder slot. */
    void setTargetObject(SpaceObject object);

    /** Does this value slot have an assignable value? */
    boolean hasTargetObject();

    /** Returns the resolved object to which the ReferenceValue refers. */
    SpaceObject getTargetObject();

    /**
     * Get the assignable value that the Executor will use move to a left-hand side of
     * assignment statement or other assignment semantics.
     * For scalars this a primitive value such as a integer.
     * For {@link org.jkcsoft.space.lang.ast.AssociationDefn}, this is the SpaceOid.
     * For Java Objects, this is the actual Java Object.
     */
    V getValue();

}
