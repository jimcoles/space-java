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
 * A link in an {@link ExpressionChain}. Any expression that can act as the
 * left-side of a dotted {@link ExpressionChain}, i.e., that contain
 * named members.
 *
 * Specifically, all value expressions, which have a type, and paths in
 * a type reference of other meta object reference.
 *
 * @author Jim Coles
 */
public interface Linkable {

    boolean hasNameRef();

    NameRef getNameRef();

    boolean isValueExpr();
}
