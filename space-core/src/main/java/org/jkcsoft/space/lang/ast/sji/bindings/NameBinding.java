/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji.bindings;

/**
 * @author Jim Coles
 */
public class NameBinding {

    /** Ref to Java class or package. */
    private final String javaMetaObjectName;
    private final String spaceFQName;

    public NameBinding(String javaMetaObjectName, String spaceFQName) {
        this.javaMetaObjectName = javaMetaObjectName;
        this.spaceFQName = spaceFQName;
    }

    public Object getJavaMetaObjectName() {
        return javaMetaObjectName;
    }

    public String getSpaceFQName() {
        return spaceFQName;
    }
}
