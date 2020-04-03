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

import java.util.List;

/**
 * The algebraic notion of a Projection of coordinates. Essentially,
 * a 0-to-many dimensional coordinate system. Within the Space type system,
 * a {@link Projection} is a set of related {@link VariableDecl}s.
 *
 * <p>Often a subset of some base type system, as is the case for the Projection
 * that defines a {@link View}.
 *
 * @see View
 *
 * @author Jim Coles
 */
public interface Projection {

    /** Projections use a starting point, the root Type, and all other variables are paths from there. */
    ComplexType getRootType();

    // ===========================================================
    // Child adders
    //
    VariableDecl addVariableDecl(VariableDecl variableDecl);

    AssociationDefn addAssociationDecl(AssociationDefn associationDecl);

    List<VariableDecl> getVariables();

    StatementBlock getInitBlock();

    FunctionDefn addFunctionDefn(FunctionDefn functionDefn);

}
