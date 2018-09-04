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

import java.util.List;

/**
 * @author Jim Coles
 */
public class TypeRefImpl extends MetaReference<DatumType> implements TypeRef {

    /** Somewhat analogous to Java 'new URL(String spec)' */
    public static TypeRef newTypeRef(String typeNameSpec) {
        if (typeNameSpec == null)
            throw new IllegalArgumentException("type ref name cannot be null");
        String[] topSplits = typeNameSpec.split(":");
        String nsName = null;
        AstFactory astFactory = AstFactory.getInstance();
        TypeRefImpl typeRef = astFactory.newTypeRef(new IntrinsicSourceInfo(), null, astFactory.newMetaRefPart(null, null, nsName));
        String[] nameStrings = typeNameSpec.split(".");
        typeRef.setFirstPart(astFactory.newMetaRefPart(typeRef, new IntrinsicSourceInfo(), nameStrings));
        return typeRef;
    }

    private List<CollectionType> collectionTypes;
    private String suffix = null;

    TypeRefImpl(SourceInfo sourceInfo, List<CollectionType> collectionTypes, MetaRefPart nsRefPart) {
        super(sourceInfo, MetaType.TYPE, nsRefPart);
        this.collectionTypes = collectionTypes;
    }

    TypeRefImpl(DatumType typeDefn) {
        super(typeDefn);
    }

    TypeRefImpl(DatumType typeDefn, List<CollectionType> collectionTypes) {
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

    public boolean isWildcard() {
        return getLastPart().isWildcard();
    }

    @Override
    public DatumType getResolvedType() {
        return getResolvedMetaObj();
    }

    public enum CollectionType {
        SEQUENCE,
        SET
    }

}


