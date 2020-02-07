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

import org.jkcsoft.space.lang.instance.NullValue;
import org.jkcsoft.space.lang.instance.ScalarValue;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jim Coles
 */
public abstract class PrimitiveTypeDefn extends AbstractDatumTypeDefn implements SimpleType {

    private static Map<String, PrimitiveTypeDefn> enumsByName = new TreeMap<>();

    protected static void addPrimitiveTypeDefn(PrimitiveTypeDefn ptDefn) {
        enumsByName.put(ptDefn.getName(), ptDefn);
    }

    public static PrimitiveTypeDefn valueOf(String name) {
        return enumsByName.get(name);
    }

    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------

    public static Map<String, PrimitiveTypeDefn> getEnumsByName() {
        return enumsByName;
    }

    PrimitiveTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public int getScalarDofs() {
        return 1;
    }

    @Override
    public MetaType getMetaType() {
        return super.getMetaType();
    }

    public ScalarValue nullValue() {
        return NullValue.NULL_VALUE;
    }

    @Override
    public boolean isPrimitiveType() {
        return true;
    }
}
