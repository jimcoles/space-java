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

import java.util.Collection;

/**
 * Base collective notion. Objects in a {@link ValueCollection} are not accessed by name
 * and their type must adhere to the collection's type constraints.
 *
 * @author Jim Coles
 */
public interface ValueCollection<T extends ValueHolder> extends SpaceObject, Iterable<T> {

    ValueCollection<T> addValue(ValueHolder holder);

}