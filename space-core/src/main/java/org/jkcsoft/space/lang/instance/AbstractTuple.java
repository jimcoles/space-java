/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.DatumDecl;
import org.jkcsoft.space.lang.ast.TypeDefn;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class AbstractTuple extends AbstractSpaceObject implements Tuple, ExeContext
{
    private byte[] creatorId;
    final private ValueHolder<Value<Object>, Object>[] valueHolders;

    protected AbstractTuple(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
        valueHolders = new ValueHolder[defn.getScalarDofs()];
    }

    @Override
    public TypeDefn getType() {
        return (TypeDefn) getDefn();
    }

    @Override
    public boolean isCollective() {
        return false;
    }

    @Override
    public boolean isTuple() {
        return true;
    }

    @Override
    public boolean isSingleValueWrapper() {
        return valueHolders.length == 1;
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        int idxHolder;
        if (getDefn() instanceof TypeDefn) {
            idxHolder = getIdxHolder(valueHolder.getDeclaration());
        }
        else {
            idxHolder = 0;
        }
        valueHolders[idxHolder] = valueHolder;
    }

    @Override
    public Tuple setValue(DatumDecl spaceDecl, Value value) {
        get(spaceDecl).setValue(value);
        return this;
    }

    @Override
    public Tuple setValue(int idx, Value value) {
        ValueHolder valueHolder = valueHolders[idx];
        if (valueHolder.getDeclaration().hasAssoc()) {
            if (value instanceof SpaceObject)
                valueHolder.setValue(getObjectFactory().newReferenceByOid(((SpaceObject) value).getOid()));
        }
        else
            valueHolder.setValue(value);

        return this;
    }

    @Override
    public ValueHolder get(DatumDecl member) {
        return valueHolders[getIdxHolder(member)];
    }

    public DatumDecl getDeclAt(int idx) {
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

    private int getIdxHolder(DatumDecl datumDecl) {
        return ((TypeDefn) getDefn()).getDatumDeclList().indexOf(datumDecl);
    }

    /** Get the 0-based ordinal of the specified member */
    private int getMemberIdx(SpaceOid memberOid) {
        int idxMember = -1;
        List<DatumDecl> allMembers = ((TypeDefn) getDefn()).getDatumDeclList();
        for (int idx = 0; idx < allMembers.size(); idx++) {
            if (allMembers.get(idx).getOid().equals(memberOid)) {
                idxMember = idx;
                break;
            }
        }
        return idxMember;
    }

    @Override
    public ValueCollection addValue(ValueHolder holder) {
        return this;
    }

    @Override
    public Iterator<ValueHolder<Value<Object>, Object>> iterator() {
        return Arrays.asList(valueHolders).iterator();
    }

    @Override
    public String toString() {
        return "([" + super.toString() + "] "
            + Strings.buildCommaDelList(getValueHolders(), holder -> holder.toString())
            + ")";
    }

}
