/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.lang.ast.*;

import java.lang.reflect.Method;

/**
 * Wraps Java method to implement AST Function Defn.
 *
 * @author Jim Coles
 */
public class SjiFunctionDefnImpl extends AbstractFunctionDefn implements FunctionDefn {

    private SjiTypeDefn sjiParentTypeDefn;
    //
    private Method jMethod;

    SjiFunctionDefnImpl(SjiTypeDefn parentTypeDefn, Method jMethod, String name, TypeRef returnTypeRef) {
        super(new NativeSourceInfo(jMethod), name, returnTypeRef);
        this.jMethod = jMethod;
        this.sjiParentTypeDefn = parentTypeDefn;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public DatumType getReturnType() {
        return null;
    }

    public Method getjMethod() {
        return jMethod;
    }

    public SjiTypeDefn getSjiParentTypeDefn() {
        return sjiParentTypeDefn;
    }
}
