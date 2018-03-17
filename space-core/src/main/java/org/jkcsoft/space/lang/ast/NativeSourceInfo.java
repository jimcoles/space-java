/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * @author Jim Coles
 */
public class NativeSourceInfo implements SourceInfo {

    private Object jMetaObject;

    public NativeSourceInfo(Object jMetaObject) {
        this.jMetaObject = jMetaObject;
    }

    public Object getjMetaObject() {
        return jMetaObject;
    }

    @Override
    public String toString() {
        return "" + jMetaObject;
    }

    @Override
    public FileCoord getStart() {
        return null;
    }

    @Override
    public FileCoord getStop() {
        return null;
    }
}
