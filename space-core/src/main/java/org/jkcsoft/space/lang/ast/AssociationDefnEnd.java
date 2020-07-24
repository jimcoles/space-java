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
 * @author Jim Coles
 */
public interface AssociationDefnEnd extends Named {

//    TypeRef getTypeRef();

    TypeDefn getType();

    /** The multiplicity 0, 1, or many or fixed positive integer */
    int getLowerMultiplicity();

    int getUpperMultiplicity();

    boolean isSingular();

    boolean isRequired();

}
