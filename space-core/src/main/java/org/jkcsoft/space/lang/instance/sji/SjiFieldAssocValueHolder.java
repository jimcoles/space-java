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

import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.sji.SjiFieldAssocDecl;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.instance.ValueHolder;

/**
 * @author Jim Coles
 */
public class SjiFieldAssocValueHolder implements ValueHolder {

    private SjiTuple sjiTuple;
    private SjiFieldAssocDecl sjiFieldAssocDecl;

    public SjiFieldAssocValueHolder(SjiTuple sjiTuple, SjiFieldAssocDecl sjiFieldVarDecl) {
        this.sjiTuple = sjiTuple;
        this.sjiFieldAssocDecl = sjiFieldVarDecl;
    }

    @Override
    public Declaration getDeclaration() {
        return sjiFieldAssocDecl;
    }

    @Override
    public DatumType getType() {
        return sjiFieldAssocDecl.getType();
    }

    @Override
    public void setValue(Value value) {
        try {
            sjiFieldAssocDecl.getjField().set(sjiTuple.getjObject(), value.getJavaValue());
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
            jValue = sjiFieldAssocDecl.getjField().get(sjiTuple.getjObject());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return SpaceHome.getSjiService().toSpaceValue(jValue);
    }
}
