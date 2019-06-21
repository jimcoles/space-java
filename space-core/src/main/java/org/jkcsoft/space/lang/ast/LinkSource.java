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
 * A {@link LinkSource} is a link in an {@link ExpressionChain}. It is any expression
 * that can act as the left-side of a dotted {@link ExpressionChain},
 * i.e., that contains named members.
 *
 * Specifically, either:
 * - A {@link TypedExpr}: a value expression (which inherently has a type)
 * - A Container: the name, possibly as a multi-part path, of (named reference to) a container
 *   (Directory or Type or other named thing) that would be referenced by a name path.
 *
 * @author Jim Coles
 */
public interface LinkSource {

    boolean hasNameRef();

    NameRefOrHolder getNameRef();

    boolean hasTypedExpr();

    TypedExpr getTypedExpr();

}
