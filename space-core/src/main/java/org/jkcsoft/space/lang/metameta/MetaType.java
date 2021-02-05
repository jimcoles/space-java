/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.metameta;

/**
 * Used by linker and semantic analyzer for compile-time type checking.
 *
 * @author Jim Coles
 */
public enum MetaType {
    TYPE,
    DATUM, // var, domain, assoc
    FUNCTION,
    RULE,
    PACKAGE;
//    ANY

    public static boolean isValueType(MetaType metaType) {
        return metaType == DATUM || metaType == FUNCTION;
    }
}
