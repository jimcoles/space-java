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
 * An monomial expression that is or contains a reference-by-name to a named
 * thing such as a datum or function.
 *
 * @author Jim Coles
 */
public interface ByNameMetaRef<T extends Named> extends MetaRef<T> {

    boolean isWildcard();

    NamePartExpr getExpression();

    String getNameExprText();
}
