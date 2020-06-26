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

import org.jkcsoft.space.lang.ast.LinkState;
import org.jkcsoft.space.lang.ast.TypeCheckState;

/**
 * Here is the meat of an instance-level reference object: the actual resolved target object.
 * Objects of this type will be stored in some central to/from table of {@link SpaceObject}
 * references.
 *
 * Analogous to the meta notion of {@link org.jkcsoft.space.lang.ast.MetaRef} and
 * {@link org.jkcsoft.space.lang.ast.AbstractRefExpr}.
 *
 * @author Jim Coles
 */
public abstract class AbstractReferenceValue<J> implements ReferenceValue<J> {

    /** A possibly-ephemeral in-memory storage of the object which might be lazy-loaded etc or
     * just verified to exists for subsequent access. */
    private SpaceObject resolvedObject; // package, datum defn, func defn
    private LinkState linkState = LinkState.INITIALIZED;
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;
    private String reason;
    private Exception cause;

    @Override
    public void setResovledObject(SpaceObject object) {
        this.resolvedObject = object;
    }

    @Override
    public boolean hasResolvedObject() {
        return this.resolvedObject != null;
    }

    @Override
    public SpaceObject getResolvedObject() {
        return resolvedObject;
    }

    public LinkState getLinkState() {
        return linkState;
    }

    public TypeCheckState getTypeCheckState() {
        return typeCheckState;
    }

    public void setInvalidRef(String reason, Exception cause) {
        this.reason = reason;
        this.cause = cause;
        this.linkState = LinkState.NOT_FOUND;
    }

}
