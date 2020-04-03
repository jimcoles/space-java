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
import org.jkcsoft.space.lang.ast.VariableDeclImpl;

/**
 * The value-level counterpart to a {@link VariableDeclImpl}.
 * A {@link VariableValueHolder} contains a reference to its definition, a VariableDefn, and
 * zero or one {@link ScalarValue}s.
 */
public class VariableValueHolder implements ValueHolder {

    private Tuple parentTuple;
    private VariableDecl declaration;
    private ScalarValue scalarValue;

    public VariableValueHolder(Tuple parentTuple, VariableDecl declaration, ScalarValue scalarValue) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
        this.scalarValue = scalarValue;
    }

    public VariableValueHolder(Tuple parentTuple, VariableDecl declaration) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
        this.scalarValue = declaration.getType().nullValue();
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    @Override
    public VariableDecl getDeclaration() {
        return declaration;
    }

    @Override
    public DatumType getType() {
        return declaration.getType();
    }

    public ScalarValue getScalarValue() {
        return scalarValue;
    }

    @Override
    public Value getValue() {
        return scalarValue;
    }

    @Override
    public void setValue(Value value) {
        if (!(value instanceof ScalarValue))
            this.scalarValue = (ScalarValue) value;
    }

    @Override
    public boolean hasValue() {
        return scalarValue != null;
    }

    @Override
    public String toString() {
        return ((declaration != null) ?
            "(" + declaration.getType().getName() + ")" + declaration.getName() + "=" : "(anon)")
            + (scalarValue != NullValue.NULL_VALUE ? scalarValue.toString() : "(not initialized)");
    }
}
