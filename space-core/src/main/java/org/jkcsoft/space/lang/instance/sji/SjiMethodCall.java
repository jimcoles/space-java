/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance.sji;

import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jim Coles
 */
public class SjiMethodCall {

    private SjiService sjiService = SpaceHome.getSjiService();
    private Object jObject;
    private Method jMethod;
    private Object[] jArgs;

    public SjiMethodCall(Object jObject, Method jMethod, Object[] jArgs) {
        this.jObject = jObject;
        this.jMethod = jMethod;
        this.jArgs = jArgs;
    }

    public Value call() {
        Value sValue = null;
        try {
            sValue = sjiService.toSpaceValue(jMethod.invoke(jObject, jArgs));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return sValue;
    }
}
