/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * Abstract base for language constructs that are references to {@link Named} meta objects.
 * References (see sub types) may be by name, synonym, relative path, etc.
 *
 * References are resolved at link-time.
 *
 * @author Jim Coles
 */
public abstract class AbstractRefExpr<T extends Named> extends AbstractModelElement implements MetaRef<T> {
    /** The meta object to which this ref refers, e.g. the SpaceTypeDefn or Package
     * or VariableDefn, FunctionDefn */
    private T resolvedMetaObj; // package, datum defn, func defn
    private LinkState state = LinkState.INITIALIZED;
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;

    public AbstractRefExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    @Override
    public LinkState getState() {
        return state;
    }

    @Override
    public void setState(LinkState state) {
        this.state = state;
    }

    @Override
    public void setTypeCheckState(TypeCheckState typeCheckState) {
        this.typeCheckState = typeCheckState;
    }

    @Override
    public boolean isResolved() {
        return state == LinkState.RESOLVED;
    }

    public MetaType getResolvedMetaType() {
        return resolvedMetaObj != null ? resolvedMetaObj.getMetaType() : null;
    }

    @Override
    public void setResolvedMetaObj(T resolvedMetaObj) {
        this.resolvedMetaObj = resolvedMetaObj;
    }

    public boolean hasResolvedMetaObj() {
        return resolvedMetaObj != null;
    }

    @Override
    public T getResolvedMetaObj() {
        return resolvedMetaObj;
    }

    @Override
    public boolean hasResolvedType() {
        return hasResolvedMetaObj()
            && getResolvedMetaObj() instanceof ValueExpr
            && ((ValueExpr) getResolvedMetaObj()).hasResolvedType();
    }

    @Override
    public TypeDefn getDatumType() {
        return ((ValueExpr) getResolvedMetaObj()).getDatumType();
    }

    @Override
    public ValueExpr getValueExpr() {
        return ((ValueExpr) getResolvedMetaObj());
    }

}
