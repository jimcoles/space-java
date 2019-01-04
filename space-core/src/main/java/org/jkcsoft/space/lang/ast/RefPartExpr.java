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
 * This is a central binary associative notion that holds the unresolved 'name'
 * of a meta object being referenced as well the resolved meta object itself
 * AFTER the linking/resolving phase.
 *
 * A {@link RefPartExpr} gets inserted into the AST anywhere that a named reference
 * is made to some named thing, e.g., type ref, datum ref, function ref (call).
 *
 * Something like a LISP 'con' cell.
 *
 * @author Jim Coles
 */
public class RefPartExpr<T extends Named> extends ModelElement implements Linkable, NameRef<T> {

    private NamePartExpr nameRefExpr; // package name, datum name (ref)
    // elements below set by linker
    /** The meta object to which this ref refers, e.g. the SpaceTypeDefn or Package
     * or VariableDefn, FunctionDefn */
    private T resolvedMetaObj; // package, datum defn, func defn
    private LinkState state = LinkState.INITIALIZED;
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;

    public RefPartExpr(NamePartExpr nameRefExpr) {
        super(nameRefExpr.getSourceInfo());
        this.nameRefExpr = nameRefExpr;
    }

    public LinkState getState() {
        return state;
    }

    public void setState(LinkState state) {
        this.state = state;
    }

    @Override
    public void setTypeCheckState(TypeCheckState typeCheckState) {
        this.typeCheckState = typeCheckState;
    }

    public boolean isWildcard() {
        return (getExpression() instanceof NamePartExpr
            && ((NamePartExpr) getExpression()).getNameExpr().equals("*"));
    }

    public NamePartExpr getExpression() {
        return nameRefExpr;
    }

    public boolean hasNameRef() {
        return true;
    }

    public NameRef getNameRef() {
        return this;
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
        return nameRefExpr.getNameExpr();
    }

    public T getResolvedMetaObj() {
        return resolvedMetaObj;
    }

    @Override
    public void setResolvedMetaObj(T resolvedMetaObj) {
        this.resolvedMetaObj = resolvedMetaObj;
    }

    /** Because we want user-centric syntactic simplicity, we don't know if this named
     * element is a value-holding thing until we resolve it. */
    public boolean isValueExpr() {

        if (getResolvedMetaObj() == null)
            throw new IllegalStateException("referenced object [" + nameRefExpr + "] has not yet been resolved");

        return getResolvedMetaObj().getMetaType() == MetaType.DATUM;
    }

}
