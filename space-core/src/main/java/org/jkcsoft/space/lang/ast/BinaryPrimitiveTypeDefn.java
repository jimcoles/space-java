/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.ScalarValue;

import java.util.Comparator;

/**
 * @author Jim Coles
 */
public class BinaryPrimitiveTypeDefn extends PrimitiveTypeDefn {

    //--------------------------------------------------------------------------
    // basic physical / representational notions

    public static final BinaryPrimitiveTypeDefn BIT = newInstance("bit", null);
    // TODO: describe a 'byte' as a sequence of 8 bits.
    public static final BinaryPrimitiveTypeDefn BYTE = newInstance("byte", null);

    private static BinaryPrimitiveTypeDefn newInstance(String name, Comparator<ScalarValue> comparator) {
        BinaryPrimitiveTypeDefn ptDefn = new BinaryPrimitiveTypeDefn(new IntrinsicSourceInfo(), name, comparator);
        PrimitiveTypeDefn.addPrimitiveTypeDefn(ptDefn);
        return ptDefn;
    }

    //-------------------------------------------------------------------------
    //
    //-------------------------------------------------------------------------

    private boolean isByteKey;
    private Comparator<ScalarValue> comparator;

    private BinaryPrimitiveTypeDefn(SourceInfo sourceInfo, String name, Comparator<ScalarValue> comparator) {
        super(sourceInfo, name);
        this.comparator = comparator;
    }

    @Override
    public boolean isByteKey() {
        return isByteKey;
    }

    @Override
    public Comparator<ScalarValue> getValueComparator() {
        return comparator;
    }
}
