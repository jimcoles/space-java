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
 * A {@link Named} {@link ExpressionChain} used by {@link ProjectionDecl}.
 *
 * @author Jim Coles
 */
public class AliasedMetaRef<T extends Named> extends NamedElement {

    private final ExpressionChain<T> metaRefExpr;

    /** The 'alias' is the 'name' of the {@link NamedElement} parent. */
    protected AliasedMetaRef(SourceInfo sourceInfo, String name, ExpressionChain<T> metaRefExpr) {
        super(sourceInfo, name);
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
}
