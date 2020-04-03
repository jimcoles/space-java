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

import java.util.List;

/**
 * @author Jim Coles
 */
public interface ComplexType extends DatumType, Projection {

    boolean hasDatums();

    List<Declaration> getDatumDeclList();

    void setGroupingNode(boolean isGroupingNode);

    @Override
    default boolean isSimpleType() {
        return false;
    }

    @Override
    default boolean isComplexType() {
        return true;
    }

    @Override
    default boolean isPrimitiveType() {
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
