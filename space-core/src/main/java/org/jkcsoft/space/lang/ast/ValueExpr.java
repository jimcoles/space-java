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
 * Any expression that may be evaluated to obtain a (typed) Value.
 *
 * @author Jim Coles
 */
public interface ValueExpr extends LinkSource {

    boolean hasResolvedType();

    TypeDefn getDatumType();

    boolean hasRef();

    MetaRef getRef();

    @Override
    default boolean hasNameRef() {
        return false;
    }

    @Override
    default NameRefOrHolder getNameRef() {
        return null;
    }

    @Override
    default boolean isValueExpr() {
        return true;
    }

    @Override
    default ValueExpr getValueExpr() {
        return this;
    }
}
