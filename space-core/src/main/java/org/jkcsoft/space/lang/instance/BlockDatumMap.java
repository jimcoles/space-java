/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.ContextDatumDefn;
import org.jkcsoft.space.lang.ast.Declaration;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jim Coles
 */
public class BlockDatumMap implements DatumMap {

    private final ContextDatumDefn defn;
    private final ValueHolder<Value<Object>, Object>[] valueHolders;

    protected BlockDatumMap(ContextDatumDefn defn) {
        this.defn = defn;
        this.valueHolders = new ValueHolder[defn.getScalarDofs()];
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        int idxHolder = getIdxHolder(valueHolder.getDeclaration());
        valueHolders[idxHolder] = valueHolder;
    }

    @Override
    public DatumMap setValue(Declaration spaceDecl, Value value) {
        get(spaceDecl).setValue(value);
        return this;
    }

    @Override
    public DatumMap setValue(int idx, Value value) {
        ValueHolder valueHolder = valueHolders[idx];
        if (valueHolder.getDeclaration().isAssoc()) {
            if (value instanceof SpaceObject)
                valueHolder.setValue(getObjectFactory().newReferenceByOid(((SpaceObject) value).getOid()));
        }
        else
            valueHolder.setValue(value);

        return this;
    }

    private ObjectFactory getObjectFactory() {
        return ObjectFactory.getInstance();
    }

    @Override
    public ValueHolder get(Declaration member) {
        return valueHolders[getIdxHolder(member)];
    }

    public Declaration getDeclAt(int idx) {
//        return ((TypeDefn) getDefn()).getDatumDeclList().get(idx);
        return valueHolders[idx].getDeclaration();
    }

    @Override
    public List<ValueHolder> getValueHolders() {
        return Arrays.asList(valueHolders);
    }

    @Override
    public ValueHolder get(int idx) {
        return valueHolders[idx];
    }

    public int getSize() {
        return valueHolders.length;
    }

    @Override
    public Object getJavaValue() {
        Object[] values = new Object[getSize()];
        for (int idxHolder = 0; idxHolder < valueHolders.length; idxHolder++) {
            values[idxHolder] = valueHolders[idxHolder].getValue().getJavaValue();
        }
        return values;
    }

    private int getIdxHolder(Declaration datumDecl) {
        return defn.getDatumDeclList().indexOf(datumDecl);
    }

    /** Get the 0-based ordinal of the specified member */
    private int getMemberIdx(SpaceOid memberOid) {
        int idxMember = -1;
        List<Declaration> allMembers = defn.getDatumDeclList();
        for (int idx = 0; idx < allMembers.size(); idx++) {
            if (allMembers.get(idx).getOid().equals(memberOid)) {
                idxMember = idx;
                break;
            }
        }
        return idxMember;
    }

    @Override
    public String toString() {
        return "([" + super.toString() + "] "
            + Strings.buildCommaDelList(getValueHolders(), holder -> ((ValueHolder) holder).getValue().toString())
            + ")";
    }

}
