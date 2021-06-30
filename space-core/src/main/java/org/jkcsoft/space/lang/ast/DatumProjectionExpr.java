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
 * <p>A {@link DatumProjectionExpr} specifies a sub-graph, GS, of an existing type
 * graph, GT, in terms of a set of paths within GT extending from a single basis
 * type, TB. The path references a datum which may. The existing type graph is called the
 * basis. The datums within the new type graph be any of the following:
 *
 * <ul>
 *     <li>A simple projection of a single basis datum.
 *     <li>A new datum that is a function of one or more existing datums, i.e,
 *     transform rules exist between the two.
 *     <li>A new datum that is unique to this type graph with some value expression.
 * </ul>
 *
 * <p>In the general case, the complete set of {@link DatumProjectionExpr}s for a view
 * form a directed graph or tree extending from the base type.</p>
 *
 * <p>Algebraically, a {@link DatumProjectionExpr} is a projection of coordinates, or a
 * coordinate system.
 *
 * <p><b>Usage:</b> Projections are used to define View datum slots {@link ViewDefn} and Keys
 * ({@link KeyDefnImpl}. Used currently to define data slots like RDB views, but
 * could also be used to define structure-only, i.e., analogous to OOP
 * class extension, or both 'data' and 'structure'.
 *
 * @see ViewDefn
 * @see KeyDefn
 *
 * @author Jim Coles
 */
public interface DatumProjectionExpr extends DatumDecl, ProjectionContext {

    /** References either a Type or a Variable or an Association wrt the Basis type.  */
    DatumDecl getProjectedDatum();

}
