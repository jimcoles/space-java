/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.DatumDecl;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.instance.ValueHolder;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jim Coles
 */
public class StaticExeContext {

    private InternalExeContext exeContext; // parent context

    private final Map<TypeDefn, Tuple> statics = new TreeMap<>();

    public StaticExeContext(InternalExeContext exeContext) {
        this.exeContext = exeContext;
    }

    public void setValue(DatumDecl staticDatumDecl, ValueHolder newValueHolder) {
        Tuple staticDatumMap = getStaticTuple(staticDatumDecl.getType());
        if (staticDatumMap == null) {
            statics.put(staticDatumDecl.getType(), exeContext.getObjFactory().newTupleImpl(staticDatumDecl.getType()));
        }
        ValueHolder staticValueHolder = staticDatumMap.get(staticDatumDecl);
        if (staticValueHolder == null) {
            staticValueHolder = exeContext.getObjFactory().newEmptyVarHolder(staticDatumMap, staticDatumDecl);
            staticDatumMap.initHolder(staticValueHolder);
        }
        exeContext.autoCastAssign(staticValueHolder, newValueHolder);
    }

    public Tuple getStaticTuple(TypeDefn type) {
        return statics.get(type);
    }

    public ValueHolder getValue(DatumDecl staticDatumDecl) {
        return getStaticTuple(staticDatumDecl.getType()).get(staticDatumDecl);
    }

}
