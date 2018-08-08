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

import java.net.URL;
import java.util.List;

/**
 * @author Jim Coles
 */
public class TypeRef extends MetaReference<DatumType> {

    /** Somewhat analogous to Java 'new URL(String spec)' */
    public static TypeRef newTypeRef(String typeName) {
        if (typeName == null)
            throw new IllegalArgumentException("type ref name cannot be null");

        TypeRef typeRef = AstFactory.getInstance().newTypeRef(new IntrinsicSourceInfo(), null);
        String[] nameStrings = typeName.split(".");
        typeRef.setFirstPart(AstFactory.getInstance().newMetaRefPart(typeRef, new IntrinsicSourceInfo(), nameStrings));
        return typeRef;
    }

    private List<CollectionType> collectionTypes;
    private String suffix = null;

    TypeRef(SourceInfo sourceInfo, List<CollectionType> collectionTypes) {
        super(sourceInfo, MetaType.TYPE);
        this.collectionTypes = collectionTypes;
    }

    TypeRef(DatumType typeDefn) {
        super(typeDefn);
    }

    TypeRef(DatumType typeDefn, List<CollectionType> collectionTypes) {
        super(typeDefn);
        this.collectionTypes = collectionTypes;
    }

    public boolean isCollectionType() {
        return collectionTypes != null && !collectionTypes.isEmpty();
    }

    public List<CollectionType> getCollectionTypes() {
        return collectionTypes;
    }

    @Override
    protected String getSuffix() {
        if (suffix == null)
            suffix = collectionTypes != null ? buildSuffix() : null;
        return suffix;
    }

    private String buildSuffix() {
        StringBuilder suff = new StringBuilder();
        for (CollectionType collectionType : collectionTypes) {
            suff.append(collectionType != null ? collectionType.name().substring(0, 3) : "???");
        }
        return suff.toString();
    }

    public enum CollectionType {
        SEQUENCE,
        SET
    }

}


