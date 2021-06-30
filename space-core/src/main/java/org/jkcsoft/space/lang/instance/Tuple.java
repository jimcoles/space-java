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

import org.jkcsoft.space.lang.ast.TypeDefn;

/**
 * <p>A Tuple is an element of a {@link TupleSet}. Each Tuple contains values
 * which are both sequenced (~ordered) and named. A Tuple is, therefore, both
 * a list and a map.
 * Values in a Tuple can be retrieved in order or by the name of the variable.  A Tuple
 * may contain only Scalar values and Oid-based references to Tuples in other
 * Spaces.
 *
 * <p>A Tuple can be thought of as a 'smart map' in that every tuple has one-and-only-one
 * base object (and associated Oid).
 *
 * <p>Space object's are abstract. A Tuple is the user's handle (view) to access an object,
 * but the user never gets the object itself, only a view into the object.</p>
 *
 * <p>A central notion in Space. Similar to notion of Tuple in other languages like
 * Python except that our data values usually have rich meta data defined in a
 * {@link TypeDefn}.
 *
 * <p>Analogous to a Java Object except that Tuples may represent partial
 * objects.
 *
 * @author Jim Coles
 */
public interface Tuple extends
    ValueCollection<ValueHolder<Value<Object>, Object>, Value<Object>, Object>, Value<Object>,
    DatumMap,
    SpaceObject
{

    TypeDefn getType();

    boolean isSingleValueWrapper();

}
