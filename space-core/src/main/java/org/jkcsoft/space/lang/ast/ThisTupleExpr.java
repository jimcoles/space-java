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
 * Evaluates to the current tuple context.
 *
 * @author Jim Coles
 */
public class ThisTupleExpr extends PronounRefExpr<TypeDefn> {

    ThisTupleExpr(SourceInfo sourceInfo) {
        super(sourceInfo, "this");
    }

    @Override
    public TypeDefn getDatumType() {
        return getResolvedMetaObj();
    }

    @Override
    public ScopeKind getScopeKind() {
        return ScopeKind.OBJECT;
    }
}
