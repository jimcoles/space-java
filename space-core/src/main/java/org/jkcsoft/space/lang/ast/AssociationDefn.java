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
 * Captures a wide range of relationships such as one-to-many, recursive.
 * Analogous to a foreign key relationship in RDB world or a simple
 * object reference in Java.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class AssociationDefn<T extends NamedElement> extends NamedElement {

    private MetaReference<SpaceTypeDefn> fromTypeRef;
    private int fromMult;   // Defaults to "many" if assoc is declared within a Space Type Defn.

    private MetaReference<T> toTypeRef;
    private int toMult;     // Defaults to 1 if assoc is declared within a Space Type Defn.

    AssociationDefn(SourceInfo sourceInfo, String name, SpacePathExpr fromPath, SpacePathExpr toPath) {
        super(sourceInfo, name);

        if (fromPath != null) {
            this.fromTypeRef = new MetaReference<>(fromPath, MetaType.TYPE);
            addChild(fromTypeRef);
        }

        if (toPath == null) throw new RuntimeException("bug: path to class ref cannot be null");
        this.toTypeRef = new MetaReference<>(toPath, MetaType.TYPE);
        //
        addChild(toTypeRef);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public MetaReference<SpaceTypeDefn> getFromTypeRef() {
        return fromTypeRef;
    }

    public MetaReference<T> getToTypeRef() {
        return toTypeRef;
    }

    public SpaceTypeDefn getFromType() {
        return fromTypeRef.getResolvedMetaObj();
    }

    public T getToType() {
        return toTypeRef.getResolvedMetaObj();
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
