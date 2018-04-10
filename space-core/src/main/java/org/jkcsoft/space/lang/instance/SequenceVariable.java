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
 * A {@link SequenceVariable} contains a reference to its definition, a VariableDefn, and
 * zero to many {@link ScalarValue}s.
 */
public class SequenceVariable<T extends ScalarValue> implements ValueHolder {

    private VariableDecl declaration;
    private Tuple parentTuple;
    private BinarySequence<T> primSeqValue;

    public SequenceVariable(Tuple parentTuple, VariableDecl declaration, BinarySequence scalarValue) {
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

    public void setPrimSeqValue(BinarySequence<T> primSeqValue) {
        this.primSeqValue = primSeqValue;
    }

    public BinarySequence getPrimSeqValue() {
        return primSeqValue;
    }

    @Override
    public DatumType getType() {
        return declaration.getType();
    }

    @Override
    public Value getValue() {
        return primSeqValue;
    }

    @Override
    public String toString() {
        return ((declaration != null) ? declaration.getName() + "=" : "(anon)")
            + (primSeqValue != null ? primSeqValue.toString() : "(not initialized)");
    }
}