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

import java.text.MessageFormat;

/**
 * @author Jim Coles
 */
public class JavaReference extends AbstractReferenceValue {

    private Object javaObject;

    JavaReference(Object javaObject) {
        this.javaObject = javaObject;
    }

    public void setJavaObject(Object javaObject) {
        this.javaObject = javaObject;
    }

    public Object getJavaObject() {
        return javaObject;
    }

    @Override
    public Object getJavaValue() {
        return javaObject;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Java object reference -> [{0}]", javaObject);
    }

}
