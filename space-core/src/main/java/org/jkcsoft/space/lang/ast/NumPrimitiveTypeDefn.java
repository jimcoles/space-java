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

import org.jkcsoft.space.lang.instance.*;

import java.util.Comparator;
import java.util.List;

/**
 * @author Jim Coles
 */
public class NumPrimitiveTypeDefn extends PrimitiveTypeDefn {

    //--------------------------------------------------------------------------
    // basic physical / representational notions

    public static final NumPrimitiveTypeDefn BIT = newInstance("bit", null);
    // TODO: describe a 'byte' as a sequence of 8 bits.
    public static final NumPrimitiveTypeDefn BYTE = newInstance("byte", null);

    // TODO: a char is a 1 to n-byte 'key' to a finite well-known external abstract object
    public static final NumPrimitiveTypeDefn CHAR =
        newInstance("char", Comparator.comparing(CharacterValue::getJavaValue));

    //--------------------------------------------------------------------------
    // Logical notions, idealizations

    public static final NumPrimitiveTypeDefn BOOLEAN =
        newInstance("boolean", Comparator.comparingInt(BooleanValue::getSortValue));
    public static final NumPrimitiveTypeDefn CARD =
        newInstance("int", Comparator.comparingLong(CardinalValue::getJavaValue));
    public static final NumPrimitiveTypeDefn REAL =
        newInstance("real", Comparator.comparingDouble(RealValue::getJavaValue));
    //
    public static final NumPrimitiveTypeDefn NULL = newInstance("null", (Comparator<NullValue>) (o1, o2) -> 0);

    private static <T extends ScalarValue> NumPrimitiveTypeDefn newInstance(String name, Comparator<T> comparator) {
        NumPrimitiveTypeDefn ptDefn = new NumPrimitiveTypeDefn(new IntrinsicSourceInfo(), name, comparator);
        PrimitiveTypeDefn.addPrimitiveTypeDefn(ptDefn);
        return ptDefn;
    }

    // ------------------------------------------------------------------------
    //

    private int arrayDepth;
    private Comparator<? extends ScalarValue> comparator;

    private <T extends ScalarValue> NumPrimitiveTypeDefn(SourceInfo sourceInfo, String name, Comparator<T> comparator) {
        super(sourceInfo, name);
        this.comparator = comparator;
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
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }

    @Override
    public VariableDecl addVariableDecl(VariableDecl variableDecl) {
        return null;
    }

    @Override
    public AssociationDefn addAssociationDecl(AssociationDefn associationDecl) {
        return null;
    }

    @Override
    public List<VariableDecl> getVariablesDeclList() {
        return null;
    }

    @Override
    public List<Declaration> getDatumDeclList() {
        return null;
    }

    @Override
    public StatementBlock getInitBlock() {
        return null;
    }

    @Override
    public FunctionDefn addFunctionDefn(FunctionDefn functionDefn) {
        return null;
    }

    @Override
    public Comparator getTypeComparator() {
        return comparator;
    }

    @Override
    public boolean hasDatums() {
        return false;
    }

    @Override
    public boolean hasPrimaryKey() {
        return true;
    }

}
