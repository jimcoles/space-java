/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance.sji;

import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.sji.SjiPropBased;
import org.jkcsoft.space.lang.ast.sji.SjiPropVarDecl;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.instance.ValueHolder;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Jim Coles
 */
public class SjiPropVarValueHolder implements ValueHolder {

    private SjiTuple sjiTuple;
    private SjiPropVarDecl sjiPropVarDecl;

    public SjiPropVarValueHolder(SjiTuple sjiTuple, SjiPropVarDecl sjiPropVarDecl) {
        this.sjiTuple = sjiTuple;
        this.sjiPropVarDecl = sjiPropVarDecl;
    }

    @Override
    public Declaration getDeclaration() {
        return sjiPropVarDecl;
    }

    @Override
    public DatumType getType() {
        return sjiPropVarDecl.getType();
    }

    @Override
    public void setValue(Value value) {
        try {
            sjiPropVarDecl.getjPropDesc().getWriteMethod().invoke(sjiTuple.getjObject(), value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
            jValue = sjiPropVarDecl.getjPropDesc().getReadMethod().invoke(sjiTuple.getjObject());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return SpaceHome.getSjiService().toSpaceValue(jValue);
    }

}
