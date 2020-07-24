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
 * A {@link ViewDefn} is an algebraic combination of Types and other Views.
 * The Space runtime is aware of Views and their
 * relationship to Entities and manages state values that equate between
 * these elements.
 *
 * <p>The following are specializations of a {@link ViewDefn}
 * <uL>
 *     <li>A Query is a possibly unnamed and often non-persistent view.
 *     <li>An index is a view that only specifies associations and keys in it's path, not variables.
 * </uL>
 * <p>A tree is a view declarative definition of a Tree with respect to a normalized Type graph.
 * <p>The simplest definition of a Tree is just a Type with a recursive 1-to-many
 * relationship. More complex Trees, those involving various Types, require the
 * declaration of which Associations constitute the parent-child Tree association.
 *
 * <p>Every {@link org.jkcsoft.space.lang.instance.TupleSet} will have a controlling
 * {@link ViewDefn}.
 *
 * @author Jim Coles
 */
public interface ViewDefn extends TypeDefn {

    /** Only m-to-1 associations (optionally, keys to use). No 1-to-m associations (which imply
     * a Tree, and no variables. */
    boolean isSimpleIndexDefn();

    boolean isTreeIndexDefn();

    boolean isIndexDefn();

    boolean isKeyDefn();

    boolean isTreeViewDefn();

    boolean isTableViewDefn();

    Rule getSelector();

    TypeDefn getBasisType();
}
