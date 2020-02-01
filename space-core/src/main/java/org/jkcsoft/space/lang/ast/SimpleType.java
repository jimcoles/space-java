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
public interface SimpleType extends DatumType {

    @Override
    default boolean isSimpleType() {
        return true;
    }

    @Override
    default boolean isPrimitiveType() {
        return false;
    }

    @Override
    default boolean isComplexType() {
        return false;
    }

    @Override
    default boolean isSetType() {
        return false;
    }

    @Override
    default boolean isSequenceType() {
        return false;
    }

    @Override
    default boolean isStreamType() {
        return false;
    }
}
