/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.StatementBlock;
import org.jkcsoft.space.lang.runtime.AstUtils;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.InternalExeContext;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jim Coles
 */
public class StaticExeContext {

    private InternalExeContext exeContext;
    private final BlockDatumMap staticDatumMap = new BlockDatumMap(null);

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
