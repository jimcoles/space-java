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

import org.jkcsoft.space.lang.instance.SpaceList;

/**
 * The base notion of an expression.  The subtypes are DefnExpr and ActionExpr.
 * <p>
 * Question: Is a SpaceExpr a Relation, or vice versa?
 *
 * @author J. Coles
 * @version 1.0
 */
public abstract class SpaceExpr {

    private SpaceList exprList;

    SpaceExpr() {

    }

}
