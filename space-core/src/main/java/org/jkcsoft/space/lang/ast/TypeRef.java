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
 * In the AST, a reference to a {@link TypeDefn}.
 *
 * @author Jim Coles
 */
public interface TypeRef extends TypedExpr, ModelElement {

    TypeDefn getResolvedType();

    LinkState getState();
}
