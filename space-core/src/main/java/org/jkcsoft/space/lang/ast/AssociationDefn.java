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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * The Space notion of Association is similar to the UML concept of the same
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
 * <li>Enable a simple JSON-like textualization (write and read) of a Space object set.
 * Most commonly,textualize a full graph of Space objects encompassing a range of
 * associated types.
 *
 * <li>Enable a "terse source code" textualization (write and parse) of a Space type system.
 * Eventually, the Space syntax itself will driven by a Space-in-Space description. This use
 * case will control, for example, which
 *
 * <li>Eliminate redundant state management by letting the language runtime
 * manage hierarchies (trees) and maps. In conventional OOP, tree structure management requires
 * that I set a 'parent node' reference on all child nodes AND a list of 'child nodes'.
 * In Space, I simply create an edge pair (a,b).
 *
 * <li>In its simplest form, behaves just like an OOP variable with a non-primitive type.
 *
 * </ol>
 *
 * @author Jim Coles
 */
public interface AssociationDefn extends Declaration {

    MetaType getMetaType();

    /** Satisfied by the 'to' type. */
    @Override
    DatumType getType();

    default AssociationKind getAssociationKind() {
        return AssociationKind.INDEPENDENT;
    };

    AssociationDefnEnd getFromEnd();

    AssociationDefnEnd getToEnd();

    DatumType getToType();

    boolean isRecursive();

}
