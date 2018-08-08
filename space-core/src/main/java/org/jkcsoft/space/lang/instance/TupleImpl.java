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
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declartion;
import org.jkcsoft.space.lang.ast.Named;
import org.jkcsoft.space.lang.ast.ComplexType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Conceptually, a Tuple is an element of a Relation (which is a Set of Tuples).
 * A Tuple is much like a row in a JDBC recordset.
 * Values in a Tuple can be retrieved in order or by the name of the variable.  A Tuple
 * may contain only Scalar values and Oid-based references to Tuples in other
 * Spaces.
 * <p>A Tuple can be thought of as a 'smart map' in that every tuple has one-and-only-one
 * base object (and associated Oid).
 * <p>Space object's are abstract.  A Tuple is the user's handle (view) to access an object,
 * but the user never gets the object itself, only a view into the object.</p>
 * <p>
 * - RDB Analog: Row
 * - Java Analog: Object
 *
 * @author Jim Coles
 * @version 1.0
 */
public class TupleImpl extends SpaceObject implements ExeContext, Tuple {

    private List<ValueHolder> valueHolders = new LinkedList<>();

    // seminal map
    private Map<SpaceOid, ValueHolder> indexAllByMemberOid = new HashMap<>();

    TupleImpl(SpaceOid oid, ComplexType defn) {
        super(oid, defn);
    }

    @Override
    public DatumType getType() {
        return getDefn();
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        valueHolders.add(valueHolder);
        indexAllByMemberOid.put(valueHolder.getDeclaration().getOid(), valueHolder);
    }

    public Declartion getNthMember(int idx) {
        return ((ComplexType) getDefn()).getDatumDeclList().get(idx);
    }

    /** Get the 0-based ordinal of the specified member */
    private int getMemberIdx(SpaceOid memberOid) {
        int idxMember = -1;
        List<Declartion> allMembers = ((ComplexType) getDefn()).getDatumDeclList();
        for (int idx = 0; idx < allMembers.size(); idx++) {
            if (allMembers.get(idx).getOid().equals(memberOid)) {
                idxMember = idx;
                break;
            }
        }
        return idxMember;
    }

    @Override
    public ValueHolder get(Declartion member) {
        return indexAllByMemberOid.get(member.getOid());
    }

    public ValueHolder getAssignableAt(int idx) {
        return indexAllByMemberOid.get(getNthMember(idx).getOid());
    }

    public List<ValueHolder> getValuesHolders() {
        return valueHolders;
    }

    public int getSize() {
        return valueHolders.size();
    }

    @Override
    public Reference getRefByOid(SpaceOid memberOid) {
        ValueHolder member = indexAllByMemberOid.get(memberOid);
        if (member == null)
            throw new IllegalArgumentException(memberOid + " not set");

        if (!(member instanceof Reference))
            throw new IllegalArgumentException(memberOid + " is not a reference");

        return (Reference) member;
    }

    @Override
    public String toString() {
        return "([" + super.toString() + "] " + Strings.buildCommaDelList(getValuesHolders()) + ")";
    }
}
