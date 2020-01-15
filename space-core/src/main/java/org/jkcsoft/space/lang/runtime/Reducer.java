/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

/**
 * The {@link Reducer} reduces AST expressions to simpler terms, thereby reducing runtime
 * time and space complexity. Ideally, this process will take advance of our
 * relational algebra.
 *
 * Some examples:
 * - Denormalize multi-part machines into simpler machines when DOFs of the normalized machine
 * are deemed unused. Could require data?
 * - Precompute constant elements.
 * -
 *
 * @author Jim Coles
 */
public class Reducer {

}
