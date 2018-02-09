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

import org.jkcsoft.space.lang.ast.PrimitiveType;
import org.jkcsoft.space.lang.ast.VariableDefn;

/**
 * Ordinal numbers are read-only to the User and can be obtained, for instance, as
 * the position of a Tuple is some sequence.
 *
 * @author Jim Coles
 */
public class OrdinalValue extends IntegerValue {

    public OrdinalValue(PrimitiveType type, Integer value) {
        super(type, value);
    }

}
