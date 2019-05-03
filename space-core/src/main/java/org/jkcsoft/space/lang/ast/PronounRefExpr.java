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
 * {@link NameRefExpr}.
 *
 * @author Jim Coles
 */
public class PronounRefExpr<T extends Named> extends AbstractRefExpr<T> {

    private String pronoun;

    public PronounRefExpr(SourceInfo sourceInfo, String pronoun) {
        super(sourceInfo);
        this.pronoun = pronoun;
    }

    @Override
    public boolean hasRef() {
        return false;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

    @Override
    public String toUrlString() {
        return pronoun;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }
}
