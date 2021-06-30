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
 * An end may be expressed as
 * <ul>
 *     <li>type.var: A chain of the type ref and the name of a datum in that type</li>
 *     <li>MAYBE: a hard link to a function-local variable: A</li>
 * </ul>
 * @author Jim Coles
 */
public interface AssociationEnd extends ModelElement {

    TypeDefn getEndTargetType();

    AssociationDefn getAssociationDefn();

    DatumDecl getDatumDecl();
}
