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
 * A Variable declaration defines a slot that will hold either a scalar value
 * or a link to another object.
 *
 * NOTE: I'm not sure we need both notions.
 *
 * @author Jim Coles
 */
public interface VariableDecl extends DatumDecl {

    AssociationEnd getAssocEnd();

}
