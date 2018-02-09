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

/**
 * Holds a reference to an "object", which might be an Entity Tuple or a member of a
 * Tuple.
 *
 * Might just as well be named "(Object) Reference".
 *
 * @author Jim Coles
 */
public class Association extends SpaceObject implements Assignable {

    private Tuple           parentTuple;
    private AssociationDefn defn;
    /** Oid of the referenced 'to' object */
    private SpaceOid        referenceOid;

    Association(SpaceOid oid, AssociationDefn defn, SpaceOid referenceOid) {
        super(oid);
        this.defn = defn;
        this.referenceOid = referenceOid;
    }

    public void setReferenceOid(SpaceOid referenceOid) {
        this.referenceOid = referenceOid;
    }

    public SpaceOid getReferenceOid() {
        return referenceOid;
    }

    public AssociationDefn getDefn() {
        return defn;
    }

    //    @Override
//    public String asString() {
//        return Executor.getInstance().dereference(referenceOid).toString();
//    }
}
