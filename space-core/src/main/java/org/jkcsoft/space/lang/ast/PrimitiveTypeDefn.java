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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Jim Coles
 */
public abstract class PrimitiveTypeDefn extends AbstractTypeDefn {

    private static Map<String, PrimitiveTypeDefn> enumsByName = new TreeMap<>();

    protected static void addPrimitiveTypeDefn(PrimitiveTypeDefn ptDefn) {
        enumsByName.put(ptDefn.getName(), ptDefn);
    }

    public static PrimitiveTypeDefn valueOf(String name) {
        return enumsByName.get(name);
    }
    public static Map<String, PrimitiveTypeDefn> getEnumsByName() {
        return enumsByName;
    }

    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------

    PrimitiveTypeDefn(SourceInfo sourceInfo, NamePart namePart) {
        super(sourceInfo, namePart);
    }

    @Override
    public int getScalarDofs() {
        return 1;
    }

    @Override
    public MetaType getMetaType() {
        return super.getMetaType();
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isComplex() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public KeyDefnImpl getPrimaryKeyDefn() {
        return null;
    }

    @Override
    public Set<KeyDefnImpl> getAlternateKeyDefns() {
        return null;
    }

    @Override
    public boolean isSetType() {
        return false;
    }

    @Override
    public boolean isSequenceType() {
        return false;
    }

    @Override
    public boolean isStreamType() {
        return false;
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }

    @Override
    public DatumDeclContext addVariableDecl(VariableDecl variableDecl) {
        return null;
    }

    @Override
    public List<VariableDecl> getVariablesDeclList() {
        return null;
    }

    @Override
    public List<DatumDecl> getDatumDeclList() {
        return null;
    }

    @Override
    public TypeDefn addFunctionDefn(FunctionDefn functionDefn) {
        return this;
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
