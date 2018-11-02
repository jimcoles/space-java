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
 * Encapsulates a single part of a 'dot-separated' {@link ExpressionChain}.
 *
 * @author Jim Coles
 */
public abstract class ExprLink<T extends NamedElement> extends ModelElement {

//    private ModelElement linkOrRefExpr; // package name, datum name (ref), function name, literal expr
    // elements below set by linker
    /** The meta object to which this ref refers, e.g. the SpaceTypeDefn or Package
     * or VariableDefn, FunctionDefn */
    private T resolvedMetaObj; // package, datum defn, func defn
    private LinkState state = LinkState.INITIALIZED;

    public ExprLink(SourceInfo sourceInfo) {
        super(sourceInfo);
//        this.parentPath = parentPath;
//        this.linkOrRefExpr = linkOrRefExpr;
    }

    public boolean isWildcard() {
        return (getExpression() instanceof NamePartExpr
            && ((NamePartExpr) getExpression()).getNameExpr().equals("*"));
    }

    public abstract boolean isValueExpr();

    public abstract ModelElement getExpression();

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

    public boolean isResolved() {
        return state == LinkState.RESOLVED;
    }

    public MetaType getResolvedMetaType() {
        return resolvedMetaObj != null ? resolvedMetaObj.getMetaType() : null;
    }

}
