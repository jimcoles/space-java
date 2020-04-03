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

import org.jkcsoft.space.lang.ast.DatumType;

/**
 * @author Jim Coles
 */
public class InMemorySpaceImpl extends AbstractSpaceObject implements Space {

    public InMemorySpaceImpl(SpaceOid oid, DatumType defn) {
        super(oid, defn);
    }

}
