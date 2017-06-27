package org.jkcsoft.space.lang.instance;/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

import org.jkcsoft.space.lang.ast.VariableDefn;

/**
 * The instance-level counterpart to a {@link org.jkcsoft.space.lang.ast.VariableDefn}.
 * A {@link Variable} contains a reference to its definition, a VariableDefn, and
 * zero or one {@link ScalarValue}s.
 */
public class Variable {

    private Tuple           parentTuple;
    private VariableDefn    definition;
    private ScalarValue     scalarValue;

    public Variable(Tuple parentTuple, VariableDefn definition, ScalarValue scalarValue) {
        this.parentTuple = parentTuple;
        this.definition = definition;
        this.scalarValue = scalarValue;
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    public VariableDefn getDefinition() {
        return definition;
    }

    public void setScalarValue(ScalarValue scalarValue) {
        this.scalarValue = scalarValue;
    }

    public ScalarValue getScalarValue() {
        return scalarValue;
    }
}
