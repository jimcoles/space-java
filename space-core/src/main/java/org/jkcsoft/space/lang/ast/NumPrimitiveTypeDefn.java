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

    //--------------------------------------------------------------------------
    // basic physical / representational notions

    public static final NumPrimitiveTypeDefn BIT = newInstance("bit");
    // TODO: describe a 'byte' as a sequence of 8 bits.
    public static final NumPrimitiveTypeDefn BYTE = newInstance("byte");

    // TODO: a char is a 2-byte 'key' to a finite well-known external abstract table
    public static final NumPrimitiveTypeDefn CHAR = newInstance("char");

    //--------------------------------------------------------------------------
    // Logical notions, idealizations

    public static final NumPrimitiveTypeDefn BOOLEAN = newInstance("boolean");
    //
    public static final NumPrimitiveTypeDefn CARD = newInstance("int");
    public static final NumPrimitiveTypeDefn REAL = newInstance("real");
    //
    public static final NumPrimitiveTypeDefn NULL = newInstance("null");

    private static NumPrimitiveTypeDefn newInstance(String name) {
        NumPrimitiveTypeDefn ptDefn = new NumPrimitiveTypeDefn(new IntrinsicSourceInfo(), name);
        PrimitiveTypeDefn.addPrimitiveTypeDefn(ptDefn);
        return ptDefn;
    }

    // ------------------------------------------------------------------------
    //

    private int arrayDepth;

    private NumPrimitiveTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    private boolean isArray() {
        return arrayDepth > 0;
    }

    public void setArrayDepth(int arrayDepth) {
        this.arrayDepth = arrayDepth;
    }

    public int getArrayDepth() {
        return arrayDepth;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean isAssignableTo(DatumType argsType) {
        return false;
    }
}
