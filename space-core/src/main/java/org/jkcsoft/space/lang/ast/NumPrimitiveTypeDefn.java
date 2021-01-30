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
import java.util.function.ToIntFunction;

/**
 * @author Jim Coles
 */
public class NumPrimitiveTypeDefn extends PrimitiveTypeDefn {

    //--------------------------------------------------------------------------
    // Intermediate abstractions
    // TODO: a char is a 1 to n-byte 'key' to a finite well-known external abstract object
    public static final NumPrimitiveTypeDefn CHAR =
        newInstance("char", Comparator.comparing(scalarValue -> ((CharacterValue) scalarValue).getJavaValue()));

    //--------------------------------------------------------------------------
    // Logical notions, idealizations

    public static final NumPrimitiveTypeDefn BOOLEAN =
        newInstance("boolean", Comparator.comparingInt(scalarValue -> ((BooleanValue) scalarValue).getSortValue()));

    public static final NumPrimitiveTypeDefn CARD =
        newInstance("int", Comparator.comparingLong(scalarValue -> ((CardinalValue) scalarValue).getJavaValue()));
    public static final NumPrimitiveTypeDefn REAL =
        newInstance("real", Comparator.comparingDouble(scalarValue -> ((RealValue) scalarValue).getJavaValue()));
    //
//    public static final NumPrimitiveTypeDefn NULL = newInstance("null", (Comparator<NullValue>) (o1, o2) -> 0);

    private static NumPrimitiveTypeDefn newInstance(String name, Comparator<ScalarValue> comparator) {
        NumPrimitiveTypeDefn ptDefn = new NumPrimitiveTypeDefn(new IntrinsicSourceInfo(), name, comparator);
        PrimitiveTypeDefn.addPrimitiveTypeDefn(ptDefn);
        return ptDefn;
    }

    // ------------------------------------------------------------------------
    //

    private int arrayDepth;
    private Comparator<ScalarValue> comparator;

    private NumPrimitiveTypeDefn(SourceInfo sourceInfo, String name, Comparator<ScalarValue> comparator) {
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
    public ContextDatumDefn addVariableDecl(VariableDecl variableDecl) {
        return null;
    }

    @Override
    public ContextDatumDefn addAssociationDecl(AssociationDefn associationDecl) {
        return null;
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        return null;
    }

    public Comparator<ScalarValue> getValueComparator() {
        return comparator;
    }
}
