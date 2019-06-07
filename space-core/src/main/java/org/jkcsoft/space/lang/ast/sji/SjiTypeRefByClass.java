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
public class SjiTypeRefByClass extends AbstractModelElement implements TypeRef {

    private SjiTypeMapping mapping;

    public SjiTypeRefByClass(Object jParent, SjiTypeMapping sjiTypeMapping) {
        super(new NativeSourceInfo(jParent));

        this.mapping = sjiTypeMapping;

        if (sjiTypeMapping.getJavaClass() == Boolean.TYPE) {
            mapping.setSpaceWrapper(NumPrimitiveTypeDefn.BOOLEAN);
        }
        else if (sjiTypeMapping.getJavaClass() == Integer.TYPE) {
            mapping.setSpaceWrapper(NumPrimitiveTypeDefn.CARD);
        }
        else if (sjiTypeMapping.getJavaClass() == Float.TYPE) {
            mapping.setSpaceWrapper(NumPrimitiveTypeDefn.REAL);
        }
        else if (sjiTypeMapping.getJavaClass() == Void.TYPE) {
            mapping.setSpaceWrapper(VoidType.VOID);
        }

        if (mapping.getSpaceWrapper() != null)
            mapping.setState(LinkState.RESOLVED);

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
    public LinkState getState() {
        return mapping.getState();
    }

    @Override
    public boolean hasResolvedType() {
        return getResolvedType() != null;
    }

    @Override
    public DatumType getDatumType() {
        return getResolvedType();
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

    @Override
    public boolean hasRef() {
        return false;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }

    @Override
    public String toString() {
        return "<SJIRef" +
            "fromObj=" + (getNamedParent() != null ? getNamedParent() : "") +
            " path=\"" + mapping.getJavaClass().getName() + "\"" +
            " resObj=" + (getState() == LinkState.RESOLVED ? getResolvedType() : "?") +
            '>';
    }

}
