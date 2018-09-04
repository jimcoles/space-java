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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.sji.SjiPropVarDecl;
import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.instance.Value;
import org.jkcsoft.space.lang.instance.ValueHolder;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Jim Coles
 */
public class SjiPropValueHolder implements ValueHolder {

    private SjiTuple sjiTuple;
    private SjiPropVarDecl sjiPropVarDecl;

    @Override
    public Declaration getDeclaration() {
        return sjiPropVarDecl;
    }

    @Override
    public DatumType getType() {
        return sjiPropVarDecl.getType();
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
        return toSpaceValue(jValue);
    }

    public static Value toSpaceValue(Object jValue) {
        Value sValue = null;
        if (jValue instanceof Integer)
            sValue = ObjectFactory.getInstance().newCardinalValue(((Integer) jValue));
        else if (jValue instanceof Float || jValue instanceof Double)
            sValue = ObjectFactory.getInstance().newRealValue(((Double) jValue));
        else if (jValue instanceof Boolean)
            sValue = ObjectFactory.getInstance().newBooleanValue(((Boolean) jValue));

        return sValue;
    }
}
