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

import org.jkcsoft.space.lang.ast.*;

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
public class Tuple extends SpaceObject<ModelElement> implements Assignable, ExeContext {

    private List<Assignable>    assignables = new LinkedList<>();
//    private List<ScalarValue>   values = new LinkedList<>();
//    private List<Reference>   associations = new LinkedList<>();

    // variables in definition order
//    private List<Variable> variables = new LinkedList<>();
    // seminal map
    private Map<SpaceOid, Assignable> indexAllByMemberOid = new HashMap<>();
    // redundant
//    private Map<String, ScalarValue> indexValuesByName = new HashMap<>();
//    private Map<String, Reference> indexRefsByName = new HashMap<>();

    Tuple(SpaceOid oid, TupleDefn defn) {
        super(oid, ((ModelElement) defn));
        // initialize
        if (defn.hasVariables()) {
            List<VariableDefn> varDefnList = ((TupleDefn) getDefn()).getVariableDefnList();
            for (VariableDefn variableDefn : varDefnList) {
                initVar(variableDefn);
            }
        }
        if (defn.hasAssociations()) {
            List<AssociationDefn> assocDefnList = ((TupleDefn) getDefn()).getAssociationDefnList();
            for (AssociationDefn assocDefn : assocDefnList) {
                initRef(assocDefn);
            }
        }
    }

//    Tuple(SpaceOid oid, SpaceTypeDefn defn, Assignable... assignables) {
//        this(oid, defn);
//        //
//        List<NamedElement> allMembers = getDefn().getAllMembers();
//        if (!getDefn().hasMembers() || assignables.length > allMembers.size())
//            throw new SpaceX(
//                "Too many values [" + assignables.length + "] for this space [" + allMembers.size() + "].");
//        //
//        for (int idxVar = 0; idxVar < assignables.length; idxVar++) {
//            SpaceUtils.assignOper(this, allMembers.get(idxVar), assignables[idxVar]);
//        }
//    }

    private void initRef(AssociationDefn assocDefn) {
        Reference ref = new Reference(assocDefn, null);
        assignables.add(ref);
        indexAllByMemberOid.put(ref.getDefn().getOid(), ref);
    }

    private void initVar(VariableDefn variableDefn) {
        Variable datum = new Variable(this, variableDefn, null);
        assignables.add(datum);
        indexAllByMemberOid.put(datum.getDefinition().getOid(), datum);
    }

    public NamedElement getNthMember(int idx) {
        return ((TupleDefn) getDefn()).getAllMembers().get(idx);
    }

    /** Get the 0-based ordinal of the specified member */
    private int getMemberIdx(SpaceOid memberOid) {
        int idxMember = -1;
        List<NamedElement> allMembers = ((TupleDefn) getDefn()).getAllMembers();
        for (int idx = 0; idx < allMembers.size(); idx++) {
            if (allMembers.get(idx).getOid().equals(memberOid)) {
                idxMember = idx;
                break;
            }
        }
        return idxMember;
    }

    public Assignable get(NamedElement member) {
        return indexAllByMemberOid.get(member.getOid());
    }

    public Assignable getAssignableAt(int idx) {
        return indexAllByMemberOid.get(getNthMember(idx).getOid());
    }

    public List<Assignable> getValuesHolders() {
        return assignables;
    }

    public SpaceOid getToOid(SpaceOid refMemberOid) {
        Reference reference = getRefByOid(refMemberOid);
        return reference.getToOid();
    }

    public int getSize() {
        return assignables.size();
    }

    public Reference getRefByOid(SpaceOid memberOid) {
        Assignable assignable = indexAllByMemberOid.get(memberOid);
        if (assignable == null)
            throw new IllegalArgumentException(memberOid + " not set");

        if (!(assignable instanceof Reference))
            throw new IllegalArgumentException(memberOid + " is not a reference");

        return (Reference) assignable;
    }

}
