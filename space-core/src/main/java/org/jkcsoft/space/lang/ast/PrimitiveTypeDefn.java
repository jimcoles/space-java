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

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jim Coles
 */
public class PrimitiveTypeDefn extends NamedElement implements DatumType {

    private static Map<String, PrimitiveTypeDefn> enumsByName = new TreeMap<>();

    public static final PrimitiveTypeDefn BOOLEAN = newPrimitiveTypeDefn("boolean");
    public static final PrimitiveTypeDefn CHAR = newPrimitiveTypeDefn("char");
    public static final PrimitiveTypeDefn CARD = newPrimitiveTypeDefn("card");
    public static final PrimitiveTypeDefn REAL = newPrimitiveTypeDefn("real");

    private static PrimitiveTypeDefn newPrimitiveTypeDefn(String s) {
        PrimitiveTypeDefn ptDefn = new PrimitiveTypeDefn(new IntrinsicSourceInfo(), s);
        enumsByName.put(ptDefn.getName(), ptDefn);
        return ptDefn;
    }

    private PrimitiveTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public int getScalarDofs() {
        return 1;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    public static PrimitiveTypeDefn valueOf(String name) {
        return enumsByName.get(name);
    }

//    VOID {
//    TEXT,
//    RATIONAL
}
