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
 * A NameRefExpr gets inserted into the AST anywhere that a named reference
 * is made to some named thing, e.g., type ref, datum ref, function ref (call).
 *
 * Something like a LISP 'con' cell.
 *
 * @author Jim Coles
 */
public class NameRefExpr<T extends Named> extends AbstractRefExpr<T> implements ByNameMetaRef<T>, MemberRefHolder {

    private NamePartExpr nameRefExpr; // package name, datum name (ref)
    // elements below set by linker

    public NameRefExpr(NamePartExpr nameRefExpr) {
        super(nameRefExpr.getSourceInfo());
        this.nameRefExpr = nameRefExpr;
    }

    public boolean isWildcard() {
        return (getExpression() instanceof NamePartExpr
            && ((NamePartExpr) getExpression()).getNameExpr().equals("*"));
    }

    @Override
    public NamePartExpr getExpression() {
        return nameRefExpr;
    }

    public boolean hasRef() {
        return true;
    }

    public MetaRef getRef() {
        return this;
    }

    @Override
    public String getDisplayName() {
        return getExpression().getDisplayName();
    }

    @Override
    public String getNameExprText() {
        return nameRefExpr.getNameExpr();
    }

    /** Because we want user-centric syntactic simplicity, we don't know if this named
     * element is a value-holding thing until we resolve it. */
    public boolean isValueExpr() {

        if (getResolvedMetaObj() == null)
            throw new IllegalStateException("referenced object [" + nameRefExpr + "] has not yet been resolved");

        return getResolvedMetaObj().getMetaType() == MetaType.DATUM;
    }

    @Override
    public DatumType getDatumType() {
        return null;
    }

    @Override
    public ByNameMetaRef getRefAsNameRef() {
        return this;
    }

    @Override
    public String toUrlString() {
        return nameRefExpr.getNameExpr();
    }
}
