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
 * Encapsulates a single part of a static reference path.
 * Usually (always?) a Directory or Type name, as used for Type references.
 *
 * @author Jim Coles
 */
public class SimpleExprLink<T extends NamedElement> extends ExprLink<T> {

//    private MetaReference parentPath;
    private NamePartExpr nameExpr; // package name, datum name (ref)

    public SimpleExprLink(NamePartExpr linkOrRefExpr) {
        super(linkOrRefExpr.getSourceInfo());
//        this.parentPath = parentPath;
        this.nameExpr = linkOrRefExpr;
    }

    public boolean isWildcard() {
        return ( this.getExpression() != null
                    && this.getExpression().getNameExpr().equals("*") );
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

    public String getNameExprText() {
        return nameExpr.getNameExpr();
    }

}
