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
import org.jkcsoft.space.lang.instance.Tuple;

import java.util.Comparator;

/**
 * @author Jim Coles
 */
public class ReferenceTypeDefn extends PrimitiveTypeDefn {

    public static ReferenceTypeDefn REF_TYPE_DEFN = new ReferenceTypeDefn(
        SourceInfo.INTRINSIC, new NamePart(SourceInfo.INTRINSIC, "reference")
    );

    private ReferenceTypeDefn(SourceInfo sourceInfo, NamePart namePart) {
        super(sourceInfo, namePart);
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        return null;
    }

    @Override
    public Comparator<ScalarValue> getValueComparator() {
        return null;
    }

}
