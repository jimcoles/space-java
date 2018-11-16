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
 * Encapsulates a single part of a 'dot-separated' {@link ExpressionChain}. This is
 * a central associative notion that holds the unresolved 'name' of the
 * thing being referenced (prior to linking phase) as well the resolved
 * meta object itself AFTER the linking/resolving phase.
 *
 * Something like a LISP 'con' cell.
 *
 * @author Jim Coles
 */
public abstract class  ExprLink<T extends NamedElement> extends ModelElement {
    //    private MetaReference parentPath;
    private NamePartExpr nameExpr; // package name, datum name (ref)

//    private ModelElement linkOrRefExpr; // package name, datum name (ref), function name, literal expr
    // elements below set by linker
    /** The meta object to which this ref refers, e.g. the SpaceTypeDefn or Package
     * or VariableDefn, FunctionDefn */
    private T resolvedMetaObj; // package, datum defn, func defn
    private LinkState state = LinkState.INITIALIZED;

    public ExprLink(SourceInfo sourceInfo, NamePartExpr linkOrRefExpr) {
        super(sourceInfo);
//        this.parentPath = parentPath;
//        this.linkOrRefExpr = linkOrRefExpr;
        this.nameExpr = linkOrRefExpr;
    }

    public boolean isWildcard() {
        return (getExpression() instanceof NamePartExpr
            && ((NamePartExpr) getExpression()).getNameExpr().equals("*"));
    }
    
    /** Because we want user-centric syntactic simplicity, we don't know if this named
     * element is a value-holding thing until we resolve it. */
    public boolean isValueExpr() {

        if (getResolvedMetaObj() == null)
            throw new IllegalStateException("referenced object [" + nameExpr + "] has not yet been resolved");

        return getResolvedMetaObj().getMetaType() == MetaType.DATUM;
    }

    public NamePartExpr getExpression() {
        return nameExpr;
    }

    public boolean hasRefName() {
        return true;
    }

    public String getRefName() {
        return nameExpr.getNameExpr();
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

    public boolean isResolved() {
        return state == LinkState.RESOLVED;
    }

    public MetaType getResolvedMetaType() {
        return resolvedMetaObj != null ? resolvedMetaObj.getMetaType() : null;
    }

    @Override
    public String getDisplayName() {
        return getExpression().getDisplayName();
    }

    public String getNameExprText() {
        return nameExpr.getNameExpr();
    }
}
