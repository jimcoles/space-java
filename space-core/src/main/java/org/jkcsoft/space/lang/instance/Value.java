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

/**
 * A {@link Value} is the object that gets assigned (at the Java level) to the LHS
 * {@link ValueHolder} of an assignment operation. {@link Value} objects do not have
 * type information or know the context of their Space-level declaration;
 * {@link ValueHolder} objects keep type and declaration context info.
 * Holds leaf values (with no type info) like ints, chars, byte, set and sequence.
 *
 * @author Jim Coles
 */
public interface Value<J> {

//    DatumType getType();

    J getJavaValue();
}
