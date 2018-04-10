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
public class NumPrimitiveTypeDefn extends PrimitiveTypeDefn {

    public static final NumPrimitiveTypeDefn BOOLEAN = newInstance("boolean");
    public static final NumPrimitiveTypeDefn CHAR = newInstance("char");
    public static final NumPrimitiveTypeDefn CARD = newInstance("int");
    public static final NumPrimitiveTypeDefn REAL = newInstance("real");

    private static NumPrimitiveTypeDefn newInstance(String name) {
        NumPrimitiveTypeDefn ptDefn = new NumPrimitiveTypeDefn(new IntrinsicSourceInfo(), name);
        PrimitiveTypeDefn.addPrimitiveTypeDefn(ptDefn);
        return ptDefn;
    }

    private NumPrimitiveTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

}