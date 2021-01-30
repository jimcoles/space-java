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

import org.jkcsoft.space.lang.instance.ScalarValue;
import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.util.Comparator;

/**
 * A Void type is dimensionless and therefore no value is possible.
 * @author Jim Coles
 */
public class VoidType extends PrimitiveTypeDefn {

    public static final VoidType VOID = new VoidType(new IntrinsicSourceInfo(), "void");

    static {
        PrimitiveTypeDefn.addPrimitiveTypeDefn(VOID);
    }

    private VoidType(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        return null;
    }

    @Override
    public Comparator<ScalarValue> getValueComparator() {
        throw new SpaceX("attempt to compare void values not defined");
    }
}
