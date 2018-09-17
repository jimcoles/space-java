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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * Encapsulates a single part of a reference path.
 * For now, usually (always?) a Directory or Type.
 *
 * @author Jim Coles
 */
public class MetaRefPart<T extends NamedElement> extends NamedElement {

//    private MetaReference parentPath;
    private NamePartExpr namePartExpr;
    // elements below set by linker
    /** The meta object to which this ref refers, e.g. the SpaceTypeDefn or Package
     * or VariableDefn */
    private T resolvedMetaObj;
    private LinkState state = LinkState.INITIALIZED;

//    private MetaRefPart prevRefPart;
//    private MetaRefPart nextRefPart;

    public MetaRefPart(NamePartExpr namePartExpr) {
        super(namePartExpr.getSourceInfo(), namePartExpr.getNameExpr());
//        this.parentPath = parentPath;
        this.namePartExpr = namePartExpr;
    }

//    public MetaReference getParentPath() {
//        return parentPath;
//    }

    public NamePartExpr getNamePartExpr() {
        return namePartExpr;
    }

    public T getResolvedMetaObj() {
        return resolvedMetaObj;
    }

    public void setResolvedMetaObj(T resolvedMetaObj) {
        this.resolvedMetaObj = resolvedMetaObj;
    }

    public LinkState getState() {
        return state;
    }

    public void setState(LinkState state) {
        this.state = state;
    }

//    public boolean isFirst() {
//        return prevRefPart == null;
//    }
//
//    public boolean hasNextExpr() {
//        return nextRefPart != null;
//    }
//
//    public MetaRefPart getNextRefPart() {
//        return nextRefPart;
//    }

//    public void setNextRefPart(MetaRefPart nextRefPart) {
//        this.nextRefPart = nextRefPart;
//        if (nextRefPart != null)
//            nextRefPart.setPrevRefPart(this);
//    }

//    public MetaRefPart getPrevRefPart() {
//        return prevRefPart;
//    }

//    public void setPrevRefPart(MetaRefPart prevRefPart) {
//        this.prevRefPart = prevRefPart;
//    }

    @Override
    public MetaType getMetaType() {
        return null;
    }

    public MetaRefPart copy(MetaReference parentRef) {
        return new MetaRefPart(this.getNamePartExpr());
    }

    public boolean isWildcard() {
        return getNamePartExpr().getNameExpr().equals("*");
    }

    public boolean isResolved() {
        return state == LinkState.RESOLVED;
    }
}
