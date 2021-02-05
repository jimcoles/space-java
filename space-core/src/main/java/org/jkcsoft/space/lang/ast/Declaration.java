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
 * @author Jim Coles
 */
public interface Declaration extends Expression, Identified, Named {

    TypeDefn getType();

    boolean isAssoc();

    default boolean isRef() {
        return isAssoc();
    }

    default boolean isVariable() {
        return !isAssoc();
    }

    default ProjectionDecl asVariable() {
        if (isVariable())
            return ((ProjectionDecl) this);
        else
            return null;
    };

    /** Compares the values of a given named variable (possibly a projection) between two tuples. */
    Comparators.DatumTupleComparator getDatumComparator();

}

