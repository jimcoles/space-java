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

import org.jkcsoft.space.lang.ast.AssociationDefn;
import org.jkcsoft.space.lang.ast.NamedElement;
import org.jkcsoft.space.lang.ast.SpaceTypeDefn;
import org.jkcsoft.space.lang.ast.VariableDefn;
import org.jkcsoft.space.lang.runtime.RuntimeException;

import java.util.*;

/**
 * Conceptually, a Tuple is an element of a Relation (which is a Set of Tuples).
 * A Tuple is much like a row in a JDBC recordset.
 * Values in a Tuple can be retrieved in order or by the name of the variable.  A Tuple
 * may contain only Scalar values and Oid-based references to Tuples in other
 * Spaces.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Tuple extends SpaceObject<SpaceTypeDefn> implements Assignable {

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

    Tuple(SpaceOid oid, SpaceTypeDefn defn) {
        super(oid, defn);
        // initialize
        if (defn.hasVariables()) {
            List<VariableDefn> varDefnList = getVarDefnList();
            for (VariableDefn variableDefn : varDefnList) {
                initVar(variableDefn);
            }
        }
        if (defn.hasAssociations()) {
            List<AssociationDefn> assocDefnList = defn.getAssociationDefnList();
            for (AssociationDefn assocDefn : assocDefnList) {
                initRef(assocDefn);
            }
        }
    }

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

    Tuple(SpaceOid oid, SpaceTypeDefn defn, Assignable... assignables) {
        this(oid, defn);
//        this.assignables.addAll(Arrays.asList(assignables));
        // set values based on order of variable and assoc in space type definition
        if (getVarDefnList() != null && assignables.length > getVarDefnList().size())
            throw new RuntimeException("Too many variables for this Space.");
        for (int idxVar = 0; idxVar < assignables.length; idxVar++) {
            setValue(getNthMemberOid(idxVar), assignables[idxVar]);
        }
    }

    public Tuple setValue(SpaceOid memberOid, Assignable value) {
        indexAllByMemberOid.put(memberOid, value);
        return this;
    }

    public Tuple setValue(int idx, Assignable value) {
        indexAllByMemberOid.put(getNthMemberOid(idx), value);
        return this;
    }

    private SpaceOid getNthMemberOid(int idx) {
        return getDefn().getAllMembers().get(idx).getOid();
    }

    public Assignable get(NamedElement member) {
        return indexAllByMemberOid.get(member.getOid());
    }

    public Assignable getAssignableAt(int idx) {
        return indexAllByMemberOid.get(getNthMemberOid(idx));
    }

//    public List<Assignable> getAssignables() {
//        return assignables;
//    }

    public List<Assignable> getValues() {
        return assignables;
    }

    public SpaceOid getToOid(SpaceOid refMemberOid) {
        Reference reference = getRefByOid(refMemberOid);
        return reference.getToOid();
    }

    public Reference getRefByOid(SpaceOid memberOid) {
        Assignable assignable = indexAllByMemberOid.get(memberOid);
        if (assignable == null)
            throw new IllegalArgumentException(memberOid + " not set");

        if (!(assignable instanceof Reference))
            throw new IllegalArgumentException(memberOid + " is not a reference");

        return (Reference) assignable;
    }

    private List<VariableDefn> getVarDefnList() {
        return getDefn().getVariableDefnList();
    }


}
