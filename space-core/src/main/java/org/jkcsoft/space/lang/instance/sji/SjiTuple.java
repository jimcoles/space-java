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

import org.jkcsoft.java.util.Beans;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declartion;
import org.jkcsoft.space.lang.ast.Named;
import org.jkcsoft.space.lang.ast.sji.SjiPropVarDecl;
import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.ast.sji.SjiVarDecl;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Wraps Java object to implement an AST Tuple.
 *
 * @author Jim Coles
 */
public class SjiTuple extends SpaceObject implements Tuple {

    private SjiTypeDefn sjiTypeDefn;
    // the wrapped element
    private Object jObject;
    private Map<Named, ValueHolder> valueHolderMap = new HashMap<>();

    public SjiTuple(SpaceOid oid, DatumType defn, Object jObject) {
        super(oid, defn);
        this.sjiTypeDefn = sjiTypeDefn;
        this.jObject = jObject;
    }

    @Override
    public DatumType getType() {
        return null;
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        valueHolderMap.put(valueHolder.getDeclaration(), valueHolder);
    }

    @Override
    public ValueHolder get(Declartion datumDecl) {
        ValueHolder valueHolder = valueHolderMap.get(datumDecl);
        Object jValue = null;
        if (valueHolder == null) {
            throw new IllegalStateException("value holder not initialized for " + datumDecl);
        }

        if (datumDecl instanceof SjiVarDecl)
            if (datumDecl instanceof SjiPropVarDecl) {
                Method readMethod = ((SjiPropVarDecl) datumDecl).getjPropDesc().getReadMethod();
                try {
                    jValue = readMethod.invoke(jObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        return valueHolder;
    }

    @Override
    public Reference getRefByOid(SpaceOid memberOid) {
        return null;
    }

    public SjiTypeDefn getSjiTypeDefn() {
        return sjiTypeDefn;
    }

    public Object getjObject() {
        return jObject;
    }
}
