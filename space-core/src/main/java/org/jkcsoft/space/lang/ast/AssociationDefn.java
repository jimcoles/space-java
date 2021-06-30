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
 * <p>Synonyms: Link Definition, Reference Definition</p>
 *
 * <p>The Space notion of Association is similar to the UML concept of the same
 * name. Space Associations are similar to Java fields/properties, but have a
 * richer set of attributes.
 *
 * <p>A named usage or relationship of one type by another type.
 * Captures a wide range of relationships such as one-to-many, recursive.
 * Analogous to a foreign key relationship in RDB world or a simple
 * field declaration (of a non-primitive type) in Java.
 *
 * <p>The set of language-specified (versus user-specified) Association attributes is
 * driven by the following key Use Cases:<ol>
 *
 * <li>Eliminate redundant state management by letting the language runtime
 * manage all collections other than a Space's basis sets: derived sets, lists, and trees.
 * With conventional OOP, a tree's management requires
 * that I set a 'parent node' reference on all child nodes AND a list of 'child nodes'
 * on all parent nodes. With Space, I simply create an link (a,b).
 * <li>Enable a simple JSON-like textualization (write and read) of a Space's object set.
 * Most commonly, textualize a full graph of Space objects encompassing a range of
 * associated types.
 * <li>Enable a "terse source code" textualization (write and parse) of a Space type system.
 * Eventually, the Space syntax itself will be driven by a Space-in-Space description. This use
 * case will control, for example, which objects get written in a full code block versus
 * written as enumerated names.
 * <li>In its simplest form, behaves just like an OOP variable with a non-scalar type.
 * </ol>
 * <p>Forms of associations:
 *   <ul>
 *   </ul>
 * </p>
 * <p>Example:
 * <pre>
 *     type DependentType {
 *          // independent vars and refs
 *          var string name;
 *          var real cost;
 *          var time creationTime;
 *          var spaceTime;
 *          var Address myAddress;      // an OID ref to an unkeyed complex object owned by this object
 *          ref IndependentType aRelatedThing;  // OID ref to a keyed object; will hold an oid ref to an IndependentType
 *          //
 *          var {&#064; rule}} = ; // a dependent variable
 *          //
 *          key (name);
 *     }
 *
 *     type IndependentType {
 *         // the assoc kind keyword: one of  'seq', 'set',
 *         // the other end of the assoc type.datum
 *         // the name of this datum
 *
 *         // this declaration links datums on each side
 *         var DependentType[.aRelatedThing] myConsumers [{&#064; kind = sequence}];
 *     }
 *
 *     // An explicit association
 *     assoc AParentChildAsc {
 *         from DependentType aRelatedThing;
 *         to IndependentType myConsumers;
 *         kind sequence;
 *     }
 * </pre>
 * </p>
 * @author Jim Coles
 */
public interface AssociationDefn extends Expression {

    boolean hasFromEnd();

    FromAssocEnd getFromEnd();

    TypeDefn getFromType();

    ToAssocEnd getToEnd();

    TypeDefn getToType();

    boolean isRecursive();

    AssociationDefn setAssociationKind(AssociationKind kind);

    AssociationKind getAssociationKind();

}
