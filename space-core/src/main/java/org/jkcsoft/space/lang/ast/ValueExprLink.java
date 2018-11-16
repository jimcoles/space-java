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
 * Encapsulates a single part of value expression in an {@link ExpressionChain}.
 *
 * @author Jim Coles
 */
public class ValueExprLink<T extends NamedElement> extends ExprLink<T> {

    public ValueExprLink(SourceInfo sourceInfo, NamePartExpr linkOrRefExpr) {
        super(sourceInfo, linkOrRefExpr);
    }
}
