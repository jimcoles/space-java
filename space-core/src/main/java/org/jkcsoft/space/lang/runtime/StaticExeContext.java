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

import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.instance.BlockDatumMap;
import org.jkcsoft.space.lang.instance.ValueHolder;
import org.jkcsoft.space.lang.runtime.InternalExeContext;

/**
 * @author Jim Coles
 */
public class StaticExeContext {

    private InternalExeContext exeContext;
    private final BlockDatumMap staticDatumMap = exeContext.getObjFactory().newBlockDatumMap(null);

    public StaticExeContext(InternalExeContext exeContext) {
        this.exeContext = exeContext;
    }

    public void setValue(Declaration staticDatumDecl, ValueHolder newValueHolder) {
        ValueHolder staticValueHolder = staticDatumMap.get(staticDatumDecl);
        if (staticValueHolder == null) {
            staticValueHolder = exeContext.getObjFactory().newEmptyVarHolder(staticDatumMap, staticDatumDecl);
            staticDatumMap.initHolder(staticValueHolder);
        }
        exeContext.autoCastAssign(staticValueHolder, newValueHolder);
    }

    public ValueHolder getValue(Declaration staticDatumDecl) {
        return staticDatumMap.get(staticDatumDecl);
    }

}
