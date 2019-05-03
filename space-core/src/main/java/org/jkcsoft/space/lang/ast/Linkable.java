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
 * A Linkable is a link in an {@link ExpressionChain}. It is any expression
 * that can act as the left-side of a dotted {@link ExpressionChain},
 * i.e., that contains named members.
 *
 * Specifically, all value expressions, which have a type, and paths in
 * a type reference or other meta object reference.
 *
 * @author Jim Coles
 */
public interface Linkable {

}
