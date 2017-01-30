/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.ActionSequenceExpr;

/**
 * The base class (abstract?) for all Transforms.  Each Transform represents
 * an instance of a TransformDefn.  In Space, Transforms are associated with
 * and, ideally, derived from an Equation, the Equation being the logical element
 * and the Transform being the physical or pragmatic.  Multiple Transforms can
 * be derived from a single Equation.
 *
 * @author J. Coles
 * @version 1.0
 */
public interface Transform {

//    private Equation parentEquation;

    /**
     * All Transforms will implement this, eventually all through model-driven
     * approach, but in the mean time, directly.
     */
    public void transform(Space r1, Space r2) throws Exception;

}
