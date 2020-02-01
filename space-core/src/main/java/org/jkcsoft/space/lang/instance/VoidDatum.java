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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.VoidType;
import org.jkcsoft.space.lang.runtime.SpaceX;

/**
 * @author Jim Coles
 */
public class VoidDatum implements ValueHolder, Value {

    public static final VoidDatum VOID = new VoidDatum();

    private VoidDatum() {}

    @Override
    public DatumType getType() {
        return VoidType.VOID;
    }

    @Override
    public void setValue(Value value) {
        // no-op
    }

    @Override
    public Object getJvalue() {
        throw new SpaceX("attempt to get void-valued value");
//        return null;
    }

    @Override
    public Value getValue() {
        return this;
    }

    @Override
    public Declaration getDeclaration() {
        return null;
    }

}
