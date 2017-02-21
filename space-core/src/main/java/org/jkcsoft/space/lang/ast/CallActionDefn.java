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
 * Represents a call to a function.
 *
 * @author Jim Coles
 */
public class CallActionDefn extends AbstractActionDefn implements AssignableDefn {

    /** The name of some other named design-time thing such as a function.
     */
    private String              functionRefId;
    private AssignmentDefn[]    assignmentDefns;

    CallActionDefn(String functionRefId, AssignmentDefn ... assignmentDefns) {
        super(null);
        this.functionRefId = functionRefId;
        this.assignmentDefns = assignmentDefns;
    }

    public String getFunctionRefId() {
        return functionRefId;
    }

    public AssignmentDefn[] getAssignmentDefns() {
        return assignmentDefns;
    }

    @Override
    public String toString() {
        return "call to " + functionRefId + "()";
    }
}
