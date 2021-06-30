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
    private SjiTypeRefByClass returnTypeRef;

    SjiFunctionDefnImpl(SjiTypeDefn parentTypeDefn, Method jMethod, NamePart namePart, SjiTypeRefByClass returnTypeRef) {
        super(new NativeSourceInfo(jMethod), namePart);
        this.sjiParentTypeDefn = parentTypeDefn;
        this.jMethod = jMethod;
        this.returnTypeRef = returnTypeRef;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public TypeDefn getReturnType() {
        return returnTypeRef.getResolvedType();
    }

    @Override
    public DatumDecl getReturnAnonDecl() {
        return null;
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

    public SjiTypeRefByClass getReturnTypeRef() {
        if (isReturnVoid)
            throw new SpaceX("Should not call getReturnTypeRef for function with void return.");
        return returnTypeRef;
    }
}
