/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

/**
 * In the Space language, every code expression returns either a Space,
 * a Tuple, or a Scalar Value.  A Scalar value can be "auto-boxed" into a
 * Tuple, and a Tuple can be auto-boxed into an anonymous (nested) Space.
 * This means that effectively, all objects may be used in any kind of
 * expression as long as data types match.  In other words, all expression
 * are Space expressions; or, equivalently, the Space algebra has closure.
 *
 * @author Jim Coles
 */
package org.jkcsoft.space.lang.instance;