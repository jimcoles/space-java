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
public class AssociationDecl extends NamedElement implements Declartion {

    private TypeRef fromTypeRef;
    private int fromMult;   // Defaults to "many" if assoc is declared within a Space Type Defn.

    private TypeRef toTypeRef;
    private int toMult;     // Defaults to 1 if assoc is declared within a Space Type Defn.

    AssociationDecl(SourceInfo sourceInfo, String name, TypeRef fromTypeRef, TypeRef toTypeRef) {
        super(sourceInfo, name);

        if (fromTypeRef != null) {
            this.fromTypeRef = fromTypeRef;
            addChild(this.fromTypeRef);
        }

        if (toTypeRef == null) throw new RuntimeException("bug: path to class ref cannot be null");
        this.toTypeRef = toTypeRef;
        //
        addChild(this.toTypeRef);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public TypeRef getFromTypeRef() {
        return fromTypeRef;
    }

    public TypeRef getToTypeRef() {
        return toTypeRef;
    }

    public DatumType getToType() {
        return toTypeRef.getResolvedMetaObj();
    }

    @Override
    public DatumType getType() {
        return getToType();
    }

    public SpacePathExpr getFromPath() {
        return fromTypeRef.getSpacePathExpr();
    }

    public int getFromMult() {
        return fromMult;
    }

    public SpacePathExpr getToPath() {
        return toTypeRef.getSpacePathExpr();
    }

    public int getToMult() {
        return toMult;
    }

    /**
     * In Space, this expression would go in an Equation expression.
     */
    public boolean isRecursive() {
        return fromTypeRef.getResolvedMetaObj() == toTypeRef.getResolvedMetaObj();
    }
}
