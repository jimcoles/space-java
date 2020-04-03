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

import org.jkcsoft.space.lang.ast.AssociationDefn;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.LinkState;

/**
 * Holds a named (declared) reference to a tuple of a specified type. This is used
 * to hold references corresponding to {@link AssociationDefn}s.
 *
 * @param <J> The concrete Java type of the value being held.
 * @author Jim Coles
 */
public class DeclaredReferenceHolder<J> implements ReferenceValueHolder<J, ReferenceValue<J>> {

    private Tuple parentTuple;
    private Declaration declaration;
    private ReferenceValue<J> referenceValue;
    //
    private LinkState linkState = LinkState.INITIALIZED;
    private String reason;
    private Exception cause;

    DeclaredReferenceHolder(Tuple parentTuple, Declaration declaration, ReferenceValue<J> referenceValue) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
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
        return declaration;
    }

    @Override
    public DatumType getType() {
        return declaration.getType();
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
    public ReferenceValue<J> getValue() {
        return referenceValue;
    }

    @Override
    public void setValue(ReferenceValue<J> value) {

    }

    public void setInvalidRef(String reason, Exception cause) {
        this.reason = reason;
        this.cause = cause;
        linkState = LinkState.NOT_FOUND;
    }

    public Tuple getParentTuple() {
        return parentTuple;
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
