/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
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
 * A {@link Named} {@link ExpressionChain} used by {@link DatumProjectionExpr}.
 *
 * @author Jim Coles
 */
public class AliasedMetaRef<T extends Named> extends AbstractNamedElement {

    private final ExpressionChain<T> metaRefExpr;

    /** The 'alias' is the 'name' of this {@link Named} element and is thereby
     * available as a name ref from other expressions. */
    protected AliasedMetaRef(SourceInfo sourceInfo, NamePart aliasNamePart, ExpressionChain<T> metaRefExpr) {
        super(sourceInfo, aliasNamePart);
        this.metaRefExpr = metaRefExpr;
        //
        addChild(metaRefExpr);
    }

    @Override
    public MetaType getMetaType() {
        return metaRefExpr.getTargetMetaType();
    }

    public ExpressionChain<T> getMetaRefExpr() {
        return metaRefExpr;
    }

    public void addPath(ScopeKind firstRefScopeKind, DatumDecl ... datumPath) {
        metaRefExpr.addPath(firstRefScopeKind, datumPath);
    }
}
