/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;


/**
 * Represents the assignment of one variable to another, either as part of
 * an action sequence or in a function call.
 *
 * @author Jim Coles
 */
public class AssignmentDefn {

    private String          leftIdentifier;

    private AssignableDefn  rightSide;  // the relative path of object to be looked up during execution

    AssignmentDefn(String leftIdentifier, AssignableDefn rightSide) {
        this.leftIdentifier = leftIdentifier;
        this.rightSide = rightSide;
    }

    public String getLeftIdentifier() {
        return leftIdentifier;
    }

    public AssignableDefn getRightSide() {
        return rightSide;
    }

}
