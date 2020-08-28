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

/**
 * Base collective notion. Objects in a {@link ValueCollection} are not accessed by name
 * and their type must adhere to the collection's type constraints.
 *
 * @author Jim Coles
 */
public interface ValueCollection<E extends ValueHolder<V, J>, V extends Value<J>, J> extends SpaceObject, Iterable<E> {

    ValueCollection<E, V, J> addValue(E holder);

}