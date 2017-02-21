/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.AssociationDefn;

/**
 * Holds a reference to an "object", which might be an Entity Tuple or a member of a
 * Tuple.
 *
 * @author Jim Coles
 */
public class Association implements Assignable {

    /** Oid of the referenced 'to' object */
    private SpaceOid referenceOid;
    private AssociationDefn defn;

    Association(AssociationDefn defn, SpaceOid referenceOid) {
        this.defn = defn;
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
