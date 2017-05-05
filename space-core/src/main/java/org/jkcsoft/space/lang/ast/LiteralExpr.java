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
 * A instance of this type is created for every occurrence of a literal valueExpr in a source file
 * e.g., numbers.
 *
 * @author Jim Coles
 */
public class LiteralExpr implements ValueExpr {

    private Object valueExpr;

    LiteralExpr(Object valueExpr) {
        this.valueExpr = valueExpr;
    }

    public boolean isString() {
        return valueExpr != null && valueExpr instanceof String;
    }

    public boolean isInt() {
        return valueExpr != null && valueExpr instanceof Integer;
    }

    public int getAsInt() {
        return ((int) valueExpr);
    }

    public String getAsString() {
        return (String) valueExpr;
    }

}
