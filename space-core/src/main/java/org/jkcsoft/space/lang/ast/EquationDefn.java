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
 * An EquationDefn is a symbolic expression that expresses a
 * relation between two otherwise independent things (Spaces).
 * An Equation is, notionally, very similar to a Rule or a
 * grammar Production.  An Equation must at all times evaluate
 * to true if the system is to be deemed in a valid state.
 *
 * @author J. Coles
 * @version 1.0
 */
public class EquationDefn extends NamedElement {

    public EquationDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }
}
