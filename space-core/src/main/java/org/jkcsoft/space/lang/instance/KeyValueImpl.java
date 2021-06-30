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

import org.jkcsoft.space.lang.ast.DatumProjectionExpr;

/**
 * A {@link KeyValueImpl} is our representation of a user-level {@link KeyValue} in which the key is
 * a declared sequence of a {@link DatumProjectionExpr}'s variable set.
 *
 * @author Jim Coles
 */
public class KeyValueImpl implements KeyValue {

    private DatumProjectionExpr keyDefn;
    private Tuple keyValues;

    @Override
    public Object getJavaValue() {
        return keyValues.getJavaValue();
    }
}
