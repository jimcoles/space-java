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
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.List;

/**
 * @author Jim Coles
 */
public class FullTypeRefImpl extends ExpressionChain implements TypeRef {

    /** Somewhat analogous to Java 'new URL(String spec)' */
    public static FullTypeRefImpl newFullTypeRef(String typeNameSpec) {
        if (typeNameSpec == null)
            throw new IllegalArgumentException("type ref name cannot be null");
        String[] topSplits = typeNameSpec.split(":");
        String nsName = topSplits.length == 2 ? topSplits[0] : null;
        String fullName = topSplits.length == 2 ? topSplits[1] : topSplits[0];
        AstFactory astFactory = AstFactory.getInstance();
        FullTypeRefImpl fullTypeRef =
            astFactory.newTypeRef(new IntrinsicSourceInfo(), null, astFactory.newNameRefExpr(null, nsName));
        String[] nameStrings = fullName.split("\\.");
        AstUtils.addNewMetaRefParts(fullTypeRef, new IntrinsicSourceInfo(), nameStrings);
        return fullTypeRef;
    }

    // -------------------------------------------------------------------------

    private List<CollectionType> collectionTypes;
    private String suffix = null;

    FullTypeRefImpl(SourceInfo sourceInfo, List<CollectionType> collectionTypes) {
        super(sourceInfo, MetaType.TYPE);
        this.collectionTypes = collectionTypes;
    }

    FullTypeRefImpl(SourceInfo sourceInfo, DatumType typeDefn) {
        super(sourceInfo, typeDefn);
    }

    FullTypeRefImpl(SourceInfo sourceInfo, DatumType typeDefn, List<CollectionType> collectionTypes) {
        this(sourceInfo, typeDefn);
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
        return extractMetaRefPath().getLastLink().isWildcard();
    }

    public boolean isSingleton() {
        return !((ByNameMetaRef) getAllLinks().getLast()).isWildcard();
    }

    @Override
    public DatumType getResolvedType() {
        return (DatumType) getResolvedMetaObj();
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

    public enum CollectionType {
        SEQUENCE,
        SET
    }

}


