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
import org.jkcsoft.space.lang.ast.sji.SjiFieldVarDecl;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.instance.ValueHolder;

/**
 * @author Jim Coles
 */
public class SjiFieldVarValueHolder implements ValueHolder {

    private SjiService sjiService;
    private SjiTuple sjiTuple;
    private SjiFieldVarDecl fieldDecl;

    public SjiFieldVarValueHolder(SjiService sjiService, SjiTuple sjiTuple, SjiFieldVarDecl sjiFieldVarDecl) {
        this.sjiService = sjiService;
        this.sjiTuple = sjiTuple;
        this.fieldDecl = sjiFieldVarDecl;
    }

    @Override
    public Declaration getDeclaration() {
        return fieldDecl;
    }

    @Override
    public TypeDefn getType() {
        return fieldDecl.getType();
    }

    @Override
    public void setValue(Value value) {
        try {
            fieldDecl.getjField().set(sjiTuple.getjObject(), value.getJavaValue());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public Value getValue() {
        Object jValue = null;
        try {
            jValue = fieldDecl.getjField().get(sjiTuple.getjObject());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sjiService.toSpaceValue(jValue);
    }
}
