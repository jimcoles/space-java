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
import org.jkcsoft.space.lang.ast.SpecInfo;

/**
 * Holds a reference to an "object", which might be a Tuple or a member of a
 * Tuple.
 *
 * Might just as well be named "(Object) Reference".
 *
 * @author Jim Coles
 */
@SpecInfo(displayName = "reference")
public class Reference implements Assignable {

    private Tuple           parentTuple;
    private AssociationDefn defn;
    /** Oid of the referenced 'to' object */
    private SpaceOid toOid;

    Reference(AssociationDefn defn, SpaceOid toOid) {
        this.defn = defn;
        this.toOid = toOid;
    }

    public void setToOid(SpaceOid toOid) {
        this.toOid = toOid;
    }

    public SpaceOid getToOid() {
        return toOid;
    }

    public AssociationDefn getDefn() {
        return defn;
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    @Override
    public String toString() {
        return "-> " + toOid;
    }
}
