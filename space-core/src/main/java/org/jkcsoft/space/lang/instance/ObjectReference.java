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

import org.jkcsoft.space.lang.ast.CoordinateDefn;
import org.jkcsoft.space.lang.runtime.Executor;

/**
 * Holds a reference to an "object", which might be an Entity Tuple or a member of a
 * Tuple.
 *
 * @author Jim Coles
 */
public class ObjectReference extends ScalarValue {

    private SpaceOid referenceOid;

    public ObjectReference(CoordinateDefn type, SpaceOid referenceOid) {
        super(type);
        this.referenceOid = referenceOid;
    }

    public SpaceOid getReferenceOid() {
        return referenceOid;
    }

    @Override
    public String asString() {
        return Executor.getInstance().dereference(referenceOid).toString();
    }
}
