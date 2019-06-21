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
 * An expression that has an associated {@link SpaceTypeDefn}, i.e., type,
 * either by direct reference to the named type or by reference to
 * a typed datum or return value of a function. TypeExpr's may be followed
 * by a dotted reference to any member of their associated type.
 *
 * @author Jim Coles
 */
public interface TypedExpr extends LinkSource {

    DatumType getDatumType();

    boolean hasResolvedType();

    boolean isValueExpr();

    boolean hasRef();

    MetaRef getRef();
}
