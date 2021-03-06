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
 * The guts of a language reference-by-name. Most MetaRefs are {@link SimpleNameRefExpr}'s, which
 * are resolved by name; the only exceptions are {@link PronounRefExpr}'s which are resolved
 * by relative scope within classes and functions.
 *
 * @author Jim Coles
 */
public interface MetaRef<T extends Named> extends LinkSource, ValueExpr {

    String getKeyOrName();

    boolean isWildcard();

    boolean isResolved();

    void setResolvedMetaObj(T resolvedMetaObj);

    T getResolvedMetaObj();

    ScopeKind getScopeKind();

    void setState(LinkState resolved);

    LinkState getState();

    void setTypeCheckState(TypeCheckState typeCheckState);

    String toUrlString();
}
