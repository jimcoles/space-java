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

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.ComplexType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Conceptually, a Tuple is an element of a Relation (which is a Set of Tuples).
 * A Tuple is much like a row in a JDBC recordset.
 * Values in a Tuple can be retrieved in order or by the name of the variable.  A Tuple
 * may contain only Scalar values and Oid-based references to Tuples in other
 * Spaces.
 * <p>A Tuple can be thought of as a 'smart map' in that every tuple has one-and-only-one
 * base object (and associated Oid).
 * <p>Space object's are abstract.  A Tuple is the user's handle (view) to access an object,
 * but the user never gets the object itself, only a view into the object.</p>
 * <p>
 * - RDB Analog: Row
 * - Java Analog: Object
 *
 * @author Jim Coles
 * @version 1.0
 */
public class TupleImpl extends AbstractTuple implements ExeContext, Tuple {

    TupleImpl(SpaceOid oid, ComplexType defn) {
        super(oid, defn);
    }

}
