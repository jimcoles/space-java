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

/**
 * Evaluates to a Space object which may be a meta (Definition) object.
 *
 * @author Jim Coles
 * @see OperLookupExpr
 */
public class SpacePathExpr extends ModelElement {

    private boolean isTypeExpr;  // If true, evaluates to a meta (definition) object.

    private PathOperEnum oper;
    SpacePathExpr childExpr;
}
