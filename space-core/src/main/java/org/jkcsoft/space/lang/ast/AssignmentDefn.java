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

import org.jkcsoft.space.lang.instance.ScalarValue;

/**
 * Represents the assignment of one variable to another, either as part of
 * an action sequence or in a function call.
 *
 * @author Jim Coles
 */
public class AssignmentDefn {

    private String      leftIdentifier;
    private String      rightIdentifier;
    private ScalarValue rightSideValue;

    AssignmentDefn(String leftIdentifier, String rightIdentifier) {
        this.leftIdentifier = leftIdentifier;
        this.rightIdentifier = rightIdentifier;
    }

    AssignmentDefn(String leftIdentifier, ScalarValue literalValue) {
        this.leftIdentifier = leftIdentifier;
        this.rightSideValue = literalValue;
    }

    public String getLeftIdentifier() {
        return leftIdentifier;
    }

    public String getRightIdentifier() {
        return rightIdentifier;
    }

    public ScalarValue getRightSideValue() {
        return rightSideValue;
    }
}
