/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * Holds info on the 'to' end of an {@link AssociationDefn}.
 *
 * @author Jim Coles
 */
public interface ToAssocEnd extends AssociationEnd {

    /** The multiplicity 0, 1, or many or fixed positive integer */
    int getLowerMultiplicity();

    int getUpperMultiplicity();

    boolean isSingular();

    boolean isRequired();

}
