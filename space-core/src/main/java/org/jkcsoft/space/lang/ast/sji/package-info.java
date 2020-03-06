/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

/**
 * The Space Java Interface (SJI) allows Space programs to use Java API's, directly.
 *
 * Elements of this package implement Space meta-level dynamic proxies to Java meta-level
 * objects, in particular, Java type's.
 *
 * Initial SJI approach uses dynamic proxies. Other possible approaches include
 * - custom proxies
 *
 * Issues:
 * - How to wrap simple Java types like GregorianCalendar, which have only one real variable
 * and no real "key". Also have a complex set of constants affecting semantics.
 *
 * @author Jim Coles
 */
package org.jkcsoft.space.lang.ast.sji;