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
 * A {@link ProjectionDecl} is a subset of variables with respect to a reference Projection,
 * which might be a basis {@link TypeDefn}. Algebraically, a {@link ProjectionDecl} is a
 * projection of coordinates, or a coordinate system.
 *
 * <p>Projections are used to define View variables {@link ViewDefn} and Keys ({@link KeyDefn}.
 *
 * @see ViewDefn
 * @see KeyDefn
 *
 * @author Jim Coles
 */
public interface ProjectionDecl extends Declaration {

    /** Every Projection has a basis type. */
    TypeDefn getRootType();

    /** References either a Type or a Variable or an Association */
    AliasedMetaRef getTypeGraphRef();

    ProjectionDecl addChild(ProjectionDecl proj);


}
