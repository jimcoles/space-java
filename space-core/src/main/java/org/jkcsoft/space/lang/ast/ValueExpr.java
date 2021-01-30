/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * Any expression that may be evaluated to obtain a (typed) Value. Might also be called
 * "Imperative Expression", as opposed to "Declarative Expression".
 *
 * @author Jim Coles
 */
public interface ValueExpr extends TypedExpr {

    @Override
    default boolean isValueExpr() {
        return true;
    }

    @Override
    default boolean hasNameRef() {
        return false;
    }

    @Override
    default NameRefOrHolder getNameRef() {
        return null;
    }

    @Override
    default boolean hasTypedExpr() {
        return true;
    }

    @Override
    default TypedExpr getTypedExpr() {
        return this;
    }
}
