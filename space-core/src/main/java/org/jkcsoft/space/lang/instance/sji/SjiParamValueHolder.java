/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance.sji;

import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.sji.SjiParamVarDecl;
import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.instance.ValueHolder;

/**
 * @author Jim Coles
 */
public class SjiParamValueHolder implements ValueHolder {

    private Tuple tuple;
    private SjiParamVarDecl sjiParamVarDecl;

    public SjiParamValueHolder(SjiTuple tuple, SjiParamVarDecl sjiParamVarDecl) {
        this.tuple = tuple;
        this.sjiParamVarDecl = sjiParamVarDecl;
    }

    @Override
    public Declaration getDeclaration() {
        return sjiParamVarDecl;
    }

    @Override
    public TypeDefn getType() {
        return sjiParamVarDecl.getType();
    }

    @Override
    public void setValue(Value value) {
        tuple.setValue(sjiParamVarDecl, value);
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public Value getValue() {
        return tuple.get(sjiParamVarDecl).getValue();
    }
}
