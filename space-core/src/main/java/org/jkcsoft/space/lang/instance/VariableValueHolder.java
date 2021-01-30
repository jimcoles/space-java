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

import org.jkcsoft.space.lang.ast.PrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.VariableDecl;
import org.jkcsoft.space.lang.ast.VariableDeclImpl;

/**
 * The value-level counterpart to a {@link VariableDeclImpl}.
 * A {@link VariableValueHolder} contains a reference to its definition, a VariableDefn, and
 * zero or one {@link ScalarValue}s.
 */
public class VariableValueHolder implements ValueHolder {

    private DatumMap parentDatumMap;
    private VariableDecl declaration;
    private ScalarValue scalarValue;

    public VariableValueHolder(DatumMap parentDatumMap, VariableDecl declaration, ScalarValue scalarValue) {
        this.parentDatumMap = parentDatumMap;
        this.declaration = declaration;
        this.scalarValue = scalarValue;
    }

    public DatumMap getParentDatumMap() {
        return parentDatumMap;
    }

    @Override
    public VariableDecl getDeclaration() {
        return declaration;
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
        if (value instanceof ScalarValue)
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
            + (scalarValue != null ? scalarValue.toString() : "(not initialized)");
    }
}
