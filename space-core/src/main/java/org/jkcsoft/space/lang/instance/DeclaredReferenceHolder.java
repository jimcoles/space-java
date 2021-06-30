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
import org.jkcsoft.space.lang.ast.DatumDecl;

/**
 * Holds a named (declared) reference to a tuple of a specified type. This is used
 * to hold references corresponding to {@link AssociationDefn}s.
 *
 * @param <J> The concrete Java type of the value being held.
 * @author Jim Coles
 */
public class DeclaredReferenceHolder<J> implements ReferenceValueHolder<ReferenceValue<J>, J> {

    private DatumDecl declaration; // an association defn
    private DatumMap datumMap; // the 'from'
    private ReferenceValue<J> referenceValue; // the 'to'

    DeclaredReferenceHolder(DatumMap datumMap, DatumDecl declaration, ReferenceValue<J> referenceValue) {
        this.datumMap = datumMap;
        this.declaration = declaration;
        this.referenceValue = referenceValue;
    }

    @Override
    public DatumDecl getDeclaration() {
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

    public DatumMap getDatumMap() {
        return datumMap;
    }

    /**
     * This method should return the key by which the target is
     * referenced, either an internal Oid, a user-defined KeyValue. Since Java cannot provide such
     * hasValue() must return False for a {@link JavaReference}.
     * @return
     */
//    Object getKey();

    @Override
    public String toString() {
        return "Decl Ref Holder: " +((declaration != null) ?
            "(" + declaration.getType().getNamePart() + ")"
                + (declaration.hasName() ? declaration.getNamePart() : "(anon)") + "=" : "(no decl)")
            + (referenceValue != null ? referenceValue.toString() : "(not initialized)");
    }

}
