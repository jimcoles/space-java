/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.VariableDecl;

/**
 * The instance-level counterpart to a {@link VariableDecl}.
 * A {@link Variable} contains a reference to its definition, a VariableDefn, and
 * zero or one {@link ScalarValue}s.
 */
public class Variable implements ValueHolder {

    private VariableDecl declaration;
    private Tuple           parentTuple;
    private ScalarValue     scalarValue;

    public Variable(Tuple parentTuple, VariableDecl declaration, ScalarValue scalarValue) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
        this.scalarValue = scalarValue;
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    @Override
    public VariableDecl getDeclaration() {
        return declaration;
    }

    public void setScalarValue(ScalarValue scalarValue) {
        this.scalarValue = scalarValue;
    }

    public ScalarValue getScalarValue() {
        return scalarValue;
    }

    @Override
    public DatumType getType() {
        return declaration.getType();
    }

    @Override
    public Value getValue() {
        return scalarValue;
    }

    @Override
    public String toString() {
        return ((declaration != null) ? declaration.getName() + "=" : "(anon)")
            + (scalarValue != null ? scalarValue.toString() : "(not initialized)");
    }
}
