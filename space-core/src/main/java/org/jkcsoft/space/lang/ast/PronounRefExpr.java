/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * A synonym-based reference, e.g., 'this'. Not quite the same as a
 * {@link SimpleNameRefExpr}.
 *
 * @author Jim Coles
 */
public abstract class PronounRefExpr<T extends Named> extends AbstractRefExpr<T> implements ValueExpr {

    private String pronoun;

    public PronounRefExpr(SourceInfo sourceInfo, String pronoun) {
        super(sourceInfo);
        this.pronoun = pronoun;
    }

    @Override
    public String getKeyOrName() {
        return this.pronoun;
    }

    @Override
    public String toUrlString() {
        return pronoun;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean hasResolvedType() {
        return super.hasResolvedMetaObj();
    }

    @Override
    public boolean hasRef() {
        return true;
    }

    @Override
    public MetaRef getRef() {
        return this;
    }

    @Override
    public boolean hasNameRef() {
        return false;
    }

    @Override
    public NameRefOrHolder getNameRef() {
        return null;
    }

}
