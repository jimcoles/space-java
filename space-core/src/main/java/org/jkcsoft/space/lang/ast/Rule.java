/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.Space;

/**
 * <p>A {@link Rule} is a symbolic expression that relates otherwise
 * independent Variables (although in a degenerative case, a Rule
 * may reference one or zero Variables). A Rule can represent any
 * of the following notions:<ul>
 *  <li>a Mathematical equation
 *  <ul>
 *      <li>an equality constraint
 *      <li>an inequality constraint
 *  </ul>
 *  <li>a grammar Production (a pattern rule)
 *  <li>an Event definition (trigger)
 *  <li>an Event handler
 *  <li>a transform (similar to an XSL transform)
 * </ul>
 * <p>By default, an {@link Rule} must at all times evaluate to true if the system is
 * to be deemed in a valid state. However, we will likely give the programmer a means
 * of activating/deactivating Rules dynamically.
 *
 * <p>Rules might end up being just the declarative view of a program's expression set. The other
 * view would be the action sequence / statement / imperative view.
 *
 * <p>Structurally, a {@link Rule} is just a {@link Named} boolean-valued {@link OperatorExpr} that
 * must evaluate to true. A Rule may also define a variable {@link DatumProjectionExpr} as a
 * means of limiting its scope at compile-time.
 *
 * <p>Sub-types and usages:<ul>
 * <li>A {@link ViewDefn} (and Query) uses Rules to represent its selection criteria.
 * </ul>
 *
 * @author Jim Coles
 */
public interface Rule extends Named {

    boolean isEquality();

    Space getScopeSpace();

    DatumProjectionExpr getVarSpace();

    OperatorExpr getOperatorExpr();

}
