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
 * A Transform is an action sequence that is bound by an Equation.
 *
 * Similar to Operator.
 *
 * @author J. Coles
 * @version 1.0
 */
public class TransformDefn extends SpaceActionDefn {

    TransformDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

}
