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
 * An expression that corresponds to a {@link SpaceTypeDefn} and which may therefore
 * be followed by a dotted reference to any member of that type. Any {@link ValueExpr}
 * is a LeftLink and so is
 *
 * @author Jim Coles
 */
public interface TypedExpr extends Linkable {
}
