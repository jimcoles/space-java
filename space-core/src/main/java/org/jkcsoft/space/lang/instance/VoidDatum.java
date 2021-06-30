/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.DatumDecl;
import org.jkcsoft.space.lang.ast.VoidType;
import org.jkcsoft.space.lang.runtime.SpaceX;

/**
 * @author Jim Coles
 */
public class VoidDatum implements ValueHolder, Value {

    public static final VoidDatum VOID = new VoidDatum();

    private VoidDatum() {
    }

    @Override
    public TypeDefn getType() {
        return VoidType.VOID;
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public Object getJavaValue() {
        throw new SpaceX("attempt to get void-valued value");
//        return null;
    }

    @Override
    public Value getValue() {
        return this;
    }

    @Override
    public void setValue(Value value) {
        // no-op
    }

    @Override
    public DatumDecl getDeclaration() {
        return null;
    }

}
