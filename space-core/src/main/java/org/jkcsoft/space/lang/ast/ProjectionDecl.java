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

/**
 * A {@link ProjectionDecl} specifies the path to a {@link VariableDecl}, or another
 * {@link ProjectionDecl}, with respect to a basis {@link TypeDefn} or {@link ViewDefn}.
 * Algebraically, a {@link ProjectionDecl} is a projection of coordinates, or a coordinate system.
 *
 * <p>Projections are used to define View variables {@link ViewDefn} and Keys ({@link KeyDefnImpl}.
 *
 * @see ViewDefn
 * @see KeyDefnImpl
 *
 * @author Jim Coles
 */
public interface ProjectionDecl extends Declaration {

    /** Every Projection has a basis type. */
    AliasedMetaRef getBasisTypeRef();

    /** References either a Type or a Variable or an Association */
    AliasedMetaRef getTypeGraphRef();

}
