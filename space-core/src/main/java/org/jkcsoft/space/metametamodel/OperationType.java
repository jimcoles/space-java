/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.metametamodel;

/**
 * An enumeration.
 */
public class OperationType {
    public static final OperationType VIEW = new OperationType(1);
    public static final OperationType EDIT = new OperationType(2);
    public static final OperationType CREATE = new OperationType(3);

    private OperationType(int i) {
    }
}