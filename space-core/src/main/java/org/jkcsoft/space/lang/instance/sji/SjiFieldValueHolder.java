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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.sji.SjiFieldVarDecl;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.instance.ValueHolder;

/**
 * @author Jim Coles
 */
public class SjiFieldValueHolder implements ValueHolder {

    private SjiTuple sjiTuple;
    private SjiFieldVarDecl fieldVarDecl;

    public SjiFieldValueHolder(SjiTuple sjiTuple, SjiFieldVarDecl fieldVarDecl) {
        this.sjiTuple = sjiTuple;
        this.fieldVarDecl = fieldVarDecl;
    }

    @Override
    public Declaration getDeclaration() {
        return null;
    }

    @Override
    public DatumType getType() {
        return null;
    }

    @Override
    public void setValue(Value value) {
        try {
            fieldVarDecl.getjField().set(sjiTuple.getjObject(), value.getJvalue());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Value getValue() {
        Object jValue = null;
        try {
            jValue = fieldVarDecl.getjField().get(sjiTuple.getjObject());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return SjiUtil.toSpaceValue(jValue);
    }
}
