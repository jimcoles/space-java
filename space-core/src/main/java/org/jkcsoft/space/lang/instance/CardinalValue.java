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

import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;

/**
 * The common notion of a IntAlgOper.  Users can set Cardinal numbers but not Ordinal
 * numbers.
 *
 * @author Jim Coles
 */
public class CardinalValue extends IntegerValue {

    /** Limit constructor access to package-only. */
    CardinalValue(Long value) {
        super(NumPrimitiveTypeDefn.CARD, value);
    }

}
