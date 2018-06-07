/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * @author Jim Coles
 */
public class MetaRefPart<T> {

    private MetaReference parentPath;
    private NamePartExpr namePartExpr;
    // elements below set by linker
    /** The meta object to which this ref refers, e.g. the SpaceTypeDefn or Package
     * or VariableDefn */
    private T resolvedMetaObj;
    private LoadState state = LoadState.INITIALIZED;

    private MetaRefPart prevRefPart;
    private MetaRefPart nextRefPart;

    public MetaRefPart(MetaReference parentPath, NamePartExpr namePartExpr) {
        this.parentPath = parentPath;
        this.namePartExpr = namePartExpr;
    }

    public NamePartExpr getNamePartExpr() {
        return namePartExpr;
    }

    public T getResolvedMetaObj() {
        return resolvedMetaObj;
    }

    public void setResolvedMetaObj(T resolvedMetaObj) {
        this.resolvedMetaObj = resolvedMetaObj;
    }

    public LoadState getState() {
        return state;
    }

    public void setState(LoadState state) {
        this.state = state;
    }

    public boolean isFirst() {
        return prevRefPart == null;
    }

    public boolean hasNextExpr() {
        return nextRefPart != null;
    }

    public MetaRefPart getNextRefPart() {
        return nextRefPart;
    }

    public void setNextRefPart(MetaRefPart nextRefPart) {
        this.nextRefPart = nextRefPart;
        if (nextRefPart != null)
            nextRefPart.setPrevRefPart(this);
    }

    public MetaRefPart getPrevRefPart() {
        return prevRefPart;
    }

    public void setPrevRefPart(MetaRefPart prevRefPart) {
        this.prevRefPart = prevRefPart;
    }

}
