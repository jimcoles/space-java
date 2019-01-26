/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * An expression that is in general on the right-side of a dotted expression
 * and therefore always contains a reference by name to a {@link Named}
 * {@link ModelElement}.
 *
 * @author Jim Coles
 */
public interface MemberRefExpr extends TypedExpr {

    NameRef getNameRef();

}
