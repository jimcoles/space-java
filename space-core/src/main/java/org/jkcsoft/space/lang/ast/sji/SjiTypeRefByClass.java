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

    SjiTypeRefByClass(Object jParent, SjiTypeMapping sjiTypeMapping) {
        super(new NativeSourceInfo(jParent));

        this.mapping = sjiTypeMapping;

        // NOTE I had been using equivalent Space types for Java primitives but
        // at this time I think it's better to use SJI proxies and then use type
        // casting if needed in expressions using Java objects, and vice versa.
//        if (sjiTypeMapping.getJavaClass() == Boolean.TYPE) {
//            mapping.setSjiProxy(NumPrimitiveTypeDefn.BOOLEAN);
//        }
//        else if (sjiTypeMapping.getJavaClass() == Integer.TYPE) {
//            mapping.setSjiProxy(NumPrimitiveTypeDefn.CARD);
//        }
//        else if (sjiTypeMapping.getJavaClass() == Float.TYPE) {
//            mapping.setSjiProxy(NumPrimitiveTypeDefn.REAL);
//        }
//        else if (sjiTypeMapping.getJavaClass() == Void.TYPE) {
//            mapping.setSjiProxy(VoidType.VOID);
//        }

        if (mapping.getSjiProxy() != null)
            mapping.setState(LinkState.RESOLVED);

    }

    public SjiTypeMapping getMapping() {
        return mapping;
    }

    public Class getWrappedClass() {
        return mapping.getJavaClass();
    }

    @Override
    public boolean hasResolvedType() {
        return mapping.getSjiProxy() != null;
    }

    @Override
    public TypeDefn getResolvedType() {
        return mapping.getSjiProxy();
    }

    @Override
    public LinkState getState() {
        return mapping.getState();
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

    @Override
    public ValueExpr getValueExpr() {
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

    @Override
    public boolean hasNameRef() {
        return false;
    }

    @Override
    public NameRefOrHolder getNameRef() {
        return null;
    }

}
