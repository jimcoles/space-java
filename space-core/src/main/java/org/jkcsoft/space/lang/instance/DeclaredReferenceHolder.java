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

import org.jkcsoft.space.lang.ast.AssociationDefn;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.Declaration;

/**
 * Holds a named (declared) reference to a tuple of a specified type. This is used
 * to hold references corresponding to {@link AssociationDefn}s.
 *
 * @param <J> The concrete Java type of the value being held.
 * @author Jim Coles
 */
public class DeclaredReferenceHolder<J> implements ReferenceValueHolder<ReferenceValue<J>, J> {

    private Declaration declaration;
    private Tuple parentTuple; // the 'from'
    private ReferenceValue<J> referenceValue; // the 'to'

    DeclaredReferenceHolder(Tuple parentTuple, Declaration declaration, ReferenceValue<J> referenceValue) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
        this.referenceValue = referenceValue;
    }

    @Override
    public Declaration getDeclaration() {
        return declaration;
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
    public void setValue(ReferenceValue<J> value) {
        this.referenceValue = value;
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    /**
     * This method should return the key by which the target is
     * referenced, either an internal Oid, a user-defined KeyValue. Since Java cannot provide such
     * hasValue() must return False for a {@link JavaReference}.
     * @return
     */
//    Object getKey();

}
