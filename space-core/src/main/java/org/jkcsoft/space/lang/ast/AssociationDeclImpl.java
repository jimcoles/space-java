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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * A named usage of one type by another type.
 * Captures a wide range of relationships such as one-to-many, recursive.
 * Analogous to a foreign key relationship in RDB world or a simple
 * object reference in Java.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class AssociationDeclImpl extends NamedElement implements AssociationDecl {

    private FullTypeRefImpl fromTypeRef;
    private int fromMult;   // Defaults to "many" if assoc is declared within a Space Type Defn.

    private TypeRef     toTypeRef;
    private int toMult;     // Defaults to 1 if assoc is declared within a Space Type Defn.

    AssociationDeclImpl(SourceInfo sourceInfo, String name, FullTypeRefImpl fromTypeRef, TypeRef toTypeRef) {
        super(sourceInfo, name);

        if (fromTypeRef != null) {
            this.fromTypeRef = fromTypeRef;
            addChild(this.fromTypeRef);
        }

        if (toTypeRef == null) throw new RuntimeException("bug: path to class ref cannot be null");
        this.toTypeRef = toTypeRef;
        //
        addChild((AbstractModelElement) this.toTypeRef);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public TypeRef getFromTypeRef() {
        return fromTypeRef;
    }

    @Override
    public TypeRef getToTypeRef() {
        return toTypeRef;
    }

    @Override
    public DatumType getToType() {
        return toTypeRef.getResolvedType();
    }

    @Override
    public DatumType getType() {
        return getToType();
    }

    @Override
    public int getFromMult() {
        return fromMult;
    }

    @Override
    public int getToMult() {
        return toMult;
    }

    /**
     * In Space, this expression would go in an Equation expression.
     */
    @Override
    public boolean isRecursive() {
        return fromTypeRef.getResolvedMetaObj() == toTypeRef.getResolvedType();
    }
}
