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
 * @author Jim Coles
 */
public class TypeRef extends MetaReference<DatumType> {

    private CollectionType collectionType = null;
    private String suffix = null;

    TypeRef(SpacePathExpr path) {
        this(path, null);
    }

    TypeRef(SpacePathExpr path, CollectionType collectionType) {
        super(path, MetaType.TYPE);
        this.collectionType = collectionType;
    }

    TypeRef(DatumType typeDefn) {
        super(typeDefn);
    }

    TypeRef(DatumType typeDefn, CollectionType collectionType) {
        super(typeDefn);
        this.collectionType = collectionType;
    }

    public boolean isCollectionType() {
        return collectionType != null;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }
    public enum CollectionType {
        SEQUENCE,
        SET

    }
    @Override
    protected String getSuffix() {
        if (suffix == null)
            suffix = collectionType != null ? collectionType.name().substring(0, 3) : "";
        return suffix;
    }

}
