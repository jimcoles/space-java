/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.TypeDefn;

/**
 * @author Jim Coles
 */
public interface ObjectRefCollection
    extends ValueCollection<FreeReferenceHolder<Object>, ReferenceValue<Object>, Object>, SpaceObject
{
    TypeDefn getContainedObjectType();

    boolean isSequence();

    boolean isSet();
}