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

import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.sji.SjiPropAssocDecl;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.*;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Jim Coles
 */
public class SjiPropAssocValueHolder implements ValueHolder<ReferenceValue<Object>, Object> {

    private SjiService sjiService;
    private SjiTuple sjiFromTuple;
    private SjiPropAssocDecl sjiPropVarDecl;
    private SjiTuple sjiToTuple;
    //
    private JavaReference javaReference;

    public SjiPropAssocValueHolder(SjiService sjiService, SjiTuple sjiFromTuple, SjiPropAssocDecl sjiPropAssocDecl) {
        this.sjiService = sjiService;
        this.sjiFromTuple = sjiFromTuple;
        this.sjiPropVarDecl = sjiPropAssocDecl;
    }

    @Override
    public Declaration getDeclaration() {
        return sjiPropVarDecl;
    }

    @Override
    public TypeDefn getType() {
        return sjiPropVarDecl.getType();
    }

    @Override
    public void setValue(ReferenceValue value) {
        try {
            sjiPropVarDecl.getjPropDesc().getWriteMethod().invoke(sjiFromTuple.getjObject(), value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method should return false for all but {@link org.jkcsoft.space.lang.instance.ReferenceByKey}.
     * @return
     */
    @Override
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    public ReferenceValue getValue() {
        if (javaReference == null) {
            Object targetJavaObject = null;
            Exception javaEx = null;
            try {
                targetJavaObject = getTargetJavaObject();
            } catch (IllegalAccessException | InvocationTargetException e) {
                javaEx = e;
            }
            javaReference = ObjectFactory.getInstance().newJavaReference(targetJavaObject);
            if (javaEx != null) {
//                javaReference.setInvalidRef("invoke of Java accessor failed", javaEx);
            }
        }
        return javaReference;
    }

    public Tuple getResolvedObject() {
        if (sjiToTuple == null) {
            try {
                sjiToTuple = sjiService.getOrCreateSjiObjectProxy(getTargetJavaObject());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return sjiToTuple;
    }

    private Object getTargetJavaObject() throws IllegalAccessException, InvocationTargetException {
        return sjiPropVarDecl.getjPropDesc().getReadMethod().invoke(sjiFromTuple.getjObject());
    }
}
