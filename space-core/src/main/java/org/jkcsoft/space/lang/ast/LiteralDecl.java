/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;

/**
 * A instance of this type is created for every occurrence of a literal value in a source file
 * e.g., numbers.
 *
 * @author Jim Coles
 */
public class LiteralDecl implements AssignableDefn {

    private Object value;

    LiteralDecl(Object value) {
        this.value = value;
    }

    public boolean isString() {
        return value != null && value instanceof String;
    }

    public boolean isInt() {
        return value != null && value instanceof Integer;
    }

    public int getAsInt() {
        return ((int) value);
    }

    public String getAsString() {
        return (String) value;
    }

}
