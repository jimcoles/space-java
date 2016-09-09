/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * Captures a wide range of relationships such as one-to-many, recursive.
 *
 * @author J. Coles
 * @version 1.0
 */
public class AssociationDefn {
    private TypeDefn _child;
    private TypeDefn _parent;


    public AssociationDefn() {
    }


    /**
     * In Space, this expression would go in an Equation expression.
     */
    public boolean isRecursive() {
        return _child == _parent;
    }


}
