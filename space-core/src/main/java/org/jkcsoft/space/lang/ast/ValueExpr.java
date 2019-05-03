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
 * Any expression that may be evaluated to obtain data.  Might also be called
 * "Imperative Expression", as opposed to "Declarative Expression".
 *
 * Imperative expressions are what the Space executor (evaluator) evaluates to get
 * data values or to mutate user data.  Declarative Expressions are used build
 * data structures, to hold Imperative Expressions, and to enforce type.
 *
 * Imperative might also be broken down into 'Mutating' versus ''.
 *
 * @author Jim Coles
 */
public interface ValueExpr extends TypedExpr {

    @Override
    default boolean isValueExpr() {
        return true;
    }

}
