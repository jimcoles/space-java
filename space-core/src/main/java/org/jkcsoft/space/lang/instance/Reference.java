/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declartion;

/**
 * Holds a reference to a Space "object", which might be a Tuple or any
 * non-scalar such as a Stream or Sequence.
 *
 * @author Jim Coles
 */
public class Reference implements ValueHolder {

    private Declartion declaration;
    private Tuple           parentTuple;
    /** Oid of the referenced 'to' object */
    private SpaceOid toOid;

    Reference(Declartion declaration, Tuple parentTuple, SpaceOid toOid) {
        this.parentTuple = parentTuple;
        this.declaration = declaration;
        this.toOid = toOid;
    }

    public void setToOid(SpaceOid toOid) {
        this.toOid = toOid;
    }

    public SpaceOid getToOid() {
        return toOid;
    }

    public Declartion getDeclaration() {
        return declaration;
    }

    public Tuple getParentTuple() {
        return parentTuple;
    }

    @Override
    public DatumType getType() {
        return declaration.getType();
    }

    @Override
    public Value getValue() {
        return toOid;
    }

    @Override
    public String toString() {
        return (declaration != null ? declaration.getName() : "(anon)") + " -> " + toOid;
    }
}
