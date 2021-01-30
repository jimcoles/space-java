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
 * Structural elements:
 * <ul>
 *     <li>Projection variables
 *     <li>Computed variables
 * </ul>
 *
 * <p>A {@link ViewDefn} is a list of projection paths to variables that extend
 * from a base Type. A {@link ViewDefn} may also specify it's own variables
 * computed from base variables or other variables or constants.
 *
 * <p>Views have several uses:
 * <ul>
 *     <li>As with RDB views, the values in Views are maintained
 *     by the Space runtime as redundant data with respect to normalized basis objects.
 *     This is possible because the Space runtime is aware of a View's relationship to a
 *     Space's normalized base objects. A program may read from or write to a
 *     View within a Space when convenient.
 *
 *      <li>A View can be defined to map a possibly denormalized subset of the basis
 *      object graph for use in an API or GUI, which typically do not expose all
 *      variables of the basis Type graph. At any point in the read/write of state
 *      the Space runtime knows every View variable's relationship to
 *      basis state of the Space.</li>
 *
 *      <li>View tuples can represent object deltas (updates) for a given transaction.</li>
 * </ul>
 *
 * <p>A View is NOT a Type and can not be used as a Type although Views and Types share
 * a similar datum access structure, namely, Maps of named data.</p>
 *
 * <p>The following are specializations of a {@link ViewDefn}
 * <ul>
 *     <li>A Query is a possibly unnamed and often non-persistent view. Queries
 *     may subscribe to any events that would alter the query results.
 *
 *     <li>A Tree is a View that traverses one or more 1-to-m associations
 *     extending from the basis Type. The simplest Tree is just a Type
 *     with a recursive 1-to-m relationship. More complex Trees, those
 *     involving various Types, require the declaration of which Associations
 *     constitute the parent-child Tree association.
 * </ul>
 *
 * <p>Every {@link org.jkcsoft.space.lang.instance.TupleSet} will have a
 * controlling {@link ViewDefn}.
 *
 * @author Jim Coles
 */
public interface ViewDefn extends TypeDefn {

    /** The basis Type may itself be a View. */
    TypeDefn getBasisType();

    /** Projections of basis type variables */
    ViewDefn addProjectionDecl(ProjectionDecl projectionDecl);

    List<ProjectionDecl> getProjectionDeclList();

    /** A View may have a Selector Rule, aka., a Filter. */
    Rule getSelector();

    /** Only m-to-1 associations (optionally, keys to use). No 1-to-m associations (which imply
     * a Tree, and no variables. */
    boolean isSimpleIndexDefn();

    boolean isTreeIndexDefn();

    boolean isIndexDefn();

    boolean isKeyDefn();

    boolean isTreeViewDefn();

    boolean isTableViewDefn();

}
