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
 * Used to declare how the 'to' type is being used by the 'from' type.
 *
 * <p>ISSUE: This a good candidate for a pure rule, i.e., Association Rule.
 *
 * <p>ISSUE: Should the 'from' type def control this? Perhaps some Types can also
 * declare themselves inherently independent of dependent. Perhaps this is
 * based on whether the Type declares a primary Key.
 *
 * @author Jim Coles
 */
public enum AssociationKind {

    /** 'From' uses 'To' for a datum type. The 'to' object is an independently identifiable
     * object that may be referenced by other objects, i.e., is 'shared'. */
    INDEPENDENT_TYPE,

    /** The 'to' is 'owned' by the 'from' object and, therefore, does not have
     * its own identity. The 'to end' object can not be moved to another parent. */
    DEPENDENT_TYPE,

    /** The 'from' type is using the 'to' type for the latter's variable set. Those variables
     * are referencable as if directly declared within the 'from type'. A variant of the
     * standard 'extends' or 'is-a' relationship.
     */
    VARIABLE_GROUP,

    /** From extends To, per OOP semantics */
    EXTENDS,

    /** From is a collection of To */
    COLLECTS
}
