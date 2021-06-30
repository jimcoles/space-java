/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.TypeDefn;

import java.util.Iterator;

/**
 * Sequence impl for non-primitive elements like basic objects.
 *
 * @author Jim Coles
 */
public class ObjectRefSequenceImpl extends AbstractSequence<ReferenceValue> implements ObjectRefSequence {

    public ObjectRefSequenceImpl(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
    }

    @Override
    public TypeDefn getContainedObjectType() {
        return null;
    }

    @Override
    public boolean isSequence() {
        return false;
    }

    @Override
    public boolean isSet() {
        return false;
    }

    @Override
    public Iterator<FreeReferenceHolder<Object>> iterator() {
        return null;
    }

    @Override
    public ValueCollection<FreeReferenceHolder<Object>, ReferenceValue<Object>, Object> addValue(
        FreeReferenceHolder<Object> holder)
    {
        return null;
    }
}
