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

import org.jkcsoft.space.lang.ast.DatumDecl;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.runtime.SpaceX;

/**
 * This is a general link from one object to another used by collection types.
 * {@link FreeReferenceHolder}s do not have individual declarations because
 * they are contained in collective objects.
 *
 * @param <J> The concrete Java type of the value being held.
 * @author Jim Coles
 */
public class FreeReferenceHolder<J> implements ReferenceValueHolder<ReferenceValue<J>, J> {

    private ObjectRefCollection fromObject; // the 'from'
    private ReferenceValue<J> referenceValue;   // the 'to'

    FreeReferenceHolder(ObjectRefCollection fromObject, ReferenceValue<J> referenceValue) {
        this.fromObject = fromObject;
        this.referenceValue = referenceValue;
    }

    @Override
    public DatumDecl getDeclaration() {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return fromObject.getDefn();
    }

    @Override
    public void setValue(ReferenceValue<J> value) {
        if (! (value instanceof ReferenceValue))
            throw new SpaceX("");
        referenceValue = ((ReferenceValue<J>) value);
    }

    @Override
    public boolean hasValue() {
        return referenceValue != null;
    }

    @Override
    public ReferenceValue<J> getValue() {
        return referenceValue;
    }

    @Override
    public String toString() {
        return referenceValue.toString();
    }
}
