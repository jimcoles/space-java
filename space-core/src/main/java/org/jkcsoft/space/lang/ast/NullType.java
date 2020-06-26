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
public class NullType extends PrimitiveTypeDefn {

    public static final NullType NULL = new NullType(new IntrinsicSourceInfo(), "null");

    static {
        PrimitiveTypeDefn.addPrimitiveTypeDefn(NULL);
    }

    private NullType(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }
}
