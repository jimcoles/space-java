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
import org.jkcsoft.space.lang.ast.Declartion;
import org.jkcsoft.space.lang.ast.VoidType;

import java.util.List;

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
    public Value getValue() {
        return this;
    }

    @Override
    public Declartion getDeclaration() {
        return null;
    }

}
