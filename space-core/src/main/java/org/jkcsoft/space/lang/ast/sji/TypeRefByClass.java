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

/**
 * Holds info for references from Space to Java:
 *
 *  (key) Java class -> Space wrapper type.
 */
public class TypeRefByClass extends ModelElement implements TypeRef {

    private SjiTypeMapping mapping;

    public TypeRefByClass(Class jClass) {
        super(new NativeSourceInfo(jClass));

        this.mapping = new SjiTypeMapping(jClass);

        if (jClass == Boolean.TYPE) {
            mapping.setSpaceWrapper(NumPrimitiveTypeDefn.BOOLEAN);
        }
        else if (jClass == Integer.TYPE) {
            mapping.setSpaceWrapper(NumPrimitiveTypeDefn.CARD);
        }
        else if (jClass == Float.TYPE) {
            mapping.setSpaceWrapper(NumPrimitiveTypeDefn.REAL);
        }
        else if (jClass == Void.TYPE) {
            mapping.setSpaceWrapper(VoidType.VOID);
        }

        if (getResolvedType() != null)
            mapping.setState(LoadState.RESOLVED);

    }

    public SjiTypeMapping getMapping() {
        return mapping;
    }

    public Class getWrappedClass() {
        return mapping.getJavaClass();
    }

    @Override
    public DatumType getResolvedType() {
        return mapping.getSpaceWrapper();
    }

    @Override
    public LoadState getState() {
        return mapping.getState();
    }

}
