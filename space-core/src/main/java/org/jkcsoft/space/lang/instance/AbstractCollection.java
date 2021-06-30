/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.TypeDefn;

/**
 * A collection of {@link Value} objects. Collections hold low-level Value objects,
 * as opposed to {@link ValueHolder}s because the value type is the same for all
 * values so there is no need for ever element of the collection specify the type
 * redundantly.
 *
 * @author Jim Coles
 */
public abstract class AbstractCollection<V extends Value> extends AbstractSpaceObject implements Value {

    public AbstractCollection(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
    }

}
