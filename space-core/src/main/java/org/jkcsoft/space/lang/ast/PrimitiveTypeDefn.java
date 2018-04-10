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
public abstract class PrimitiveTypeDefn extends NamedElement implements DatumType {

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

    private SequenceTypeDefn sequenceTypeDefn;

    PrimitiveTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
        sequenceTypeDefn = new SequenceTypeDefn(getSourceInfo(), this);
    }

    @Override
    public int getScalarDofs() {
        return 1;
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        return sequenceTypeDefn;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

}
