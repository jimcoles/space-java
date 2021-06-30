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

import org.jkcsoft.space.lang.ast.NativeSourceInfo;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Jim Coles
 */
public class SjiParamVarDecl extends SjiVarDecl {

    private Method jMethod;
    private Parameter jParam;

    SjiParamVarDecl(SjiService sjiService, Parameter jParam, SjiTypeDefn sjiParentTypeDefn) {
        super(sjiService, new NativeSourceInfo(jParam), sjiParentTypeDefn,
              sjiService.newSjiNamePart(jParam, jParam.getName()));
        this.jParam = jParam;
    }

    public Parameter getjParam() {
        return jParam;
    }

    public Method getjMethod() {
        return jMethod;
    }
}
