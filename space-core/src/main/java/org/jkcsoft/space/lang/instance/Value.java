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

/**
 * Tag for classes that hold leaf values (with no type info) like ints, chars, byte,
 * set and sequence.
 *
 * @author Jim Coles
 */
public interface Value<T> {

    DatumType getType();

    T getJValue();
}
