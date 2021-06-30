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

import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.VariableDecl;

/**
 * The value-level counterpart to a Sequence of {@link TypeDefn}.
 * A {@link ScalarSequenceHolder} contains a reference to its definition, a VariableDefn, and
 * zero to many {@link ScalarValue}s.
 */
public class ScalarSequenceHolder<T extends ScalarValue> implements ValueHolder {

    private VariableDecl declaration;
    private Tuple parentTuple;
    private ScalarValueSequence<T> primSeqValue;

    public ScalarSequenceHolder(Tuple parentTuple, VariableDecl declaration, ScalarValueSequence scalarValue) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
        this.primSeqValue = scalarValue;
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    @Override
    public VariableDecl getDeclaration() {
        return declaration;
    }

    public void setPrimSeqValue(ScalarValueSequence<T> primSeqValue) {
        this.primSeqValue = primSeqValue;
    }

    public ScalarValueSequence getPrimSeqValue() {
        return primSeqValue;
    }

    @Override
    public TypeDefn getType() {
        return declaration.getType();
    }

    @Override
    public void setValue(Value value) {
        this.primSeqValue = (ScalarValueSequence) value;
    }

    @Override
    public boolean hasValue() {
        return primSeqValue != null;
    }

    @Override
    public Value getValue() {
        return primSeqValue;
    }

    @Override
    public String toString() {
        return ((declaration != null) ? declaration.getNamePart() + "=" : "(anon)")
            + (primSeqValue != null ? primSeqValue.toString() : "(not initialized)");
    }
}
