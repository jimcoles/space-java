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

import org.jkcsoft.space.lang.ast.TypeDefn;

/**
 * An abstract base class for impls of the {@link SpaceObject} interface. See
 * that source for semantics.
 *
 * @author Jim Coles
 */
public class AbstractSpaceObject implements SpaceObject {

    private TypeDefn defn;
    private SpaceOid oid;

    public AbstractSpaceObject(SpaceOid oid, TypeDefn defn) {
        this.oid = oid;
        this.defn = defn;
    }

    @Override
    public TypeDefn getDefn() {
        return defn;
    }

    @Override
    public SpaceOid getOid() {
        return oid;
    }

    // convenience
    ObjectFactory getObjectFactory() {
        return ObjectFactory.getInstance();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + this.getOid();
    }

}
