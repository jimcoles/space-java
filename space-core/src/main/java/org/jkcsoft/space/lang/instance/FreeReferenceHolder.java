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
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.LinkState;

/**
 * This is a general link from one object to another used by collection types.
 * @param <J> The concrete Java type of the value being held.
 * @author Jim Coles
 */
public class FreeReferenceHolder<J> implements ReferenceValueHolder<J, ReferenceValue<J>> {

    private TupleCollection fromObject;
    private ReferenceValue<J> referenceValue;
    //
    private LinkState linkState = LinkState.INITIALIZED;
    private String reason;
    private Exception cause;

    FreeReferenceHolder(TupleCollection fromObject, ReferenceValue<J> referenceValue) {
        this.fromObject = fromObject;
        this.referenceValue = referenceValue;
    }

    public LinkState getLinkState() {
        return linkState;
    }

    public void setLinkState(LinkState linkState) {
        this.linkState = linkState;
    }

    @Override
    public Declaration getDeclaration() {
        return null;
    }

    @Override
    public DatumType getType() {
        return fromObject.getDefn();
    }

    @Override
    public void setValue(ReferenceValue<J> value) {
        referenceValue = value;
    }

    @Override
    public boolean hasValue() {
        return referenceValue != null;
    }

    @Override
    public void setTarget(SpaceObject object) {

    }

    @Override
    public boolean hasTarget() {
        return false;
    }

    @Override
    public void setTargetObject(SpaceObject object) {

    }

    @Override
    public boolean hasTargetObject() {
        return false;
    }

    @Override
    public SpaceObject getTargetObject() {
        return null;
    }

    @Override
    public ReferenceValue<J> getValue() {
        return referenceValue;
    }

    public void setInvalidRef(String reason, Exception cause) {
        this.reason = reason;
        this.cause = cause;
        linkState = LinkState.NOT_FOUND;
    }

    public Tuple getFromObject() {
        return fromObject;
    }

    public String getReason() {
        return reason;
    }

    public Exception getCause() {
        return cause;
    }

    /**
     * This method should return the key by which the target is
     * referenced, either an internal Oid, a user-defined KeyValue. Since Java cannot provide such
     * hasValue() must return False for a {@link JavaReference}.
     * @return
     */
//    Object getKey();

}
