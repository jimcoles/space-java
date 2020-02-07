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
import org.jkcsoft.space.lang.ast.ComplexType;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jim Coles
 * @version 1.0
 */
public class AbstractTuple extends SpaceObject implements ExeContext, Tuple {

    private ValueHolder[] valueHolders;
//    private List<ValueHolder> valueHolders = new LinkedList<>();
    // seminal map
//    private Map<SpaceOid, ValueHolder> indexAllByMemberOid = new HashMap<>();

    protected AbstractTuple(SpaceOid oid, DatumType defn) {
        super(oid, defn);
        valueHolders = new ValueHolder[defn.getScalarDofs()];
    }

    @Override
    public DatumType getType() {
        return getDefn();
    }

    @Override
    public boolean isSingleValueWrapper() {
        return valueHolders.length == 1;
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        int idxHolder;
        if (getDefn() instanceof ComplexType) {
            idxHolder = getIdxHolder(valueHolder.getDeclaration());
        }
        else {
            idxHolder = 0;
        }
        valueHolders[idxHolder] = valueHolder;
    }

    @Override
    public Tuple setValue(Declaration spaceDecl, Value value) {
        get(spaceDecl).setValue(value);
        return this;
    }

    @Override
    public ValueHolder get(Declaration member) {
        return valueHolders[getIdxHolder(member)];
    }

    public Declaration getDeclAt(int idx) {
//        return ((ComplexType) getDefn()).getDatumDeclList().get(idx);
        return valueHolders[idx].getDeclaration();
    }

    /**
     * This method only valid on a Tuple if the tuple has one-and-only-one dimension.
     * @return
     */
    @Override
    public Object getJValue() {
        Object jValue = null;
        if (valueHolders.length == 1) {
//            jValue = valueHolders.get(0).getValue().getJvalue();
            jValue = valueHolders[0].getValue().getJValue();
        }
        return jValue;
    }

    public List<ValueHolder> getValueHolders() {
        return Arrays.asList(valueHolders);
    }

    public int getSize() {
        return valueHolders.length;
    }

//    @Override
//    public Reference getRefByOid(SpaceOid memberOid) {
//        ValueHolder member = indexAllByMemberOid.get(memberOid);
//        if (member == null)
//            throw new IllegalArgumentException(memberOid + " not set");
//
//        if (!(member instanceof Reference))
//            throw new IllegalArgumentException(memberOid + " is not a reference");
//
//        return (Reference) member;
//    }

//

    private int getIdxHolder(Declaration datumDecl) {
        return ((ComplexType) getDefn()).getDatumDeclList().indexOf(datumDecl);
    }

    /** Get the 0-based ordinal of the specified member */
    private int getMemberIdx(SpaceOid memberOid) {
        int idxMember = -1;
        List<Declaration> allMembers = ((ComplexType) getDefn()).getDatumDeclList();
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
            + Strings.buildCommaDelList(getValueHolders(), obj -> ((ValueHolder) obj).getValue().toString())
            + ")";
    }
}
