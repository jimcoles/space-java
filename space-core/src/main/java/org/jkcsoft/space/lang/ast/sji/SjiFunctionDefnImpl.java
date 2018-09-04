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

import org.jkcsoft.space.lang.ast.AbstractFunctionDefn;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.FunctionDefn;
import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.runtime.SpaceX;

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
    private boolean isReturnVoid;
    private TypeRefByClass returnTypeRef;

    SjiFunctionDefnImpl(SjiTypeDefn parentTypeDefn, Method jMethod, String name, TypeRefByClass returnTypeRef) {
        super(new NativeSourceInfo(jMethod), name);
        this.sjiParentTypeDefn = parentTypeDefn;
        this.jMethod = jMethod;
        this.returnTypeRef = returnTypeRef;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public DatumType getReturnType() {
        return returnTypeRef.getResolvedType();
    }

    public Method getjMethod() {
        return jMethod;
    }

    public SjiTypeDefn getSjiParentTypeDefn() {
        return sjiParentTypeDefn;
    }

    public boolean isReturnVoid() {
        return isReturnVoid;
    }

    public TypeRefByClass getReturnTypeRef() {
        if (isReturnVoid)
            throw new SpaceX("Should not call getReturnTypeRef for function with void return.");
        return returnTypeRef;
    }
}
