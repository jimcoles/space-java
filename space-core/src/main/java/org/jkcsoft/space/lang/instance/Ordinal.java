/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.CoordinateDefn;

/**
 * Ordinal numbers are read-only to the User and can be obtained, for instance, as
 * the position of a Tuple is some sequence.
 *
 * @author Jim Coles
 */
public class Ordinal extends IntegerValue {

    public Ordinal(SpaceOid oid, CoordinateDefn type) {
        super(oid, type);
    }

}
