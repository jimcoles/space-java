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
import java.util.Set;

/**
 * A View is a redundantly-maintained and usually denormalized look into a set of
 * related basis objects in a Space.
 *
 * <p>Structural elements:
 * <ul>
 *     <li>Simple Projection datums
 *     <li>Computed datums
 * </ul>
 *
 * <p>Structurally, a {@link ViewDefn} is a list of datum slots along with a value-expression
 * (a Rule) for deriving the datum value from other values in the Space. A {@link ViewDefn}.
 *
 * <p>While a View similar to a Type structurally, a View is NOT a Type and can not
 * be used as a Type. The key difference is that a View's datums are
 * derived. View datums can only be set directly if they map unambiguously to their
 * underlying basic type. Also, the identity of a View tuple is
 * inherited from the basis object behind the Tuple.</p>
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
 * <p>The following are specializations of a {@link ViewDefn}
 * <ul>
 *     <li>A Query is a possibly unnamed and often non-persistent view. Queries
 *     may subscribe to any events that would alter the query results.
 *
 *     <li>A Tree is a View that traverses one or more 1-to-m associations
 *     extending from the basis Type. The simplest Tree is just a Type
 *     with a recursive 1-to-m relationship. More complex Trees, those
 *     involving various Types, require the specification of which Associations
 *     constitute the parent-child Tree association.
 * </ul>
 *
 * <p>Every {@link org.jkcsoft.space.lang.instance.TupleSet} will have a
 * controlling {@link ViewDefn}.
 *
 * <p>(Data) View Example:</p>
 * <blockquote><pre>
 * view PersonView {
 * basis /Person,     // the basis type; all that is needed is a single type referenced by name
 *    Person/homeAddress as addr,     // you may include related elements if you wish to alias
 *    ./currentJob/address as workAddr
 *
 * projection      // i.e., the projection of the basis; the datums / coordinate system / dimensions of this view
 *     // spreadsheet-like expressions
 *     firstName,             // can ref by unqualified name if unambiguous
 *     base/lastName,       // ref by qualified name
 *     [base/]addr/areaCode,         // ref by alias
 *     province/code,
 *     yearlySalary = job/monthlySalary * 12, //
 *     workAddr/*,              // all scalar-valued
 *     note = "literal",        // literal values may be used?
 *     string userComment       // view-unique var
 *     ;
 *
 * selector // i.e., the filter, the 'selector' of an XSL rule or grammar production
 *      addr.countryCode =
 * }
 * </pre></blockquote>
 *
 * @author Jim Coles
 */
public interface ViewDefn extends TypeDefn, ProjectionContext {

    /** Only m-to-1 associations (optionally, keys to use). No 1-to-m associations (which imply
     * a Tree, and no variables. */
    boolean isSimpleIndexDefn();

    boolean isTreeIndexDefn();

    boolean isIndexDefn();

    boolean isKeyDefn();

    boolean isTreeViewDefn();

    boolean isTableViewDefn();

    /** The basis Type may itself be a View. */
    TypeDefn getBasisType();

    Set<TypeDefn> getProvidedInterfaces();

    /** Projections of basis type variables and associations */
    ViewDefn addProjectionDecl(DatumProjectionExpr datumProjectionExpr);

    List<DatumProjectionExpr> getProjectionDeclList();

    /** A View may have a Selector Rule, aka., a Filter. */
    Rule getSelector();

}
