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
 * @param <T> The class of meta object being referenced.
 * @author Jim Coles
 */
public class MetaReference<T extends Named> extends ModelElement implements ValueExpr {

    private SpacePathExpr spacePathExpr;
    private MetaType targetMetaType;

    // elements below set by linker
//    private NamedElement lexicalContext;
    /** e.g., the SpaceTypeDefn object if this ref resolves to such,
     * or VariableDefn, etc.. */
    private T resolvedMetaObj;
    private ScopeKind resolvedDatumScope;  // only relevant if target is a datum type.
    private Class<AbstractCollectionTypeDefn> collClass;
    private LoadState state = LoadState.INITIALIZED;


    MetaReference(SpacePathExpr spacePathExpr, MetaType targetMetaType) {
        this(spacePathExpr, targetMetaType, null);
    }

    MetaReference(SpacePathExpr spacePathExpr, Class<AbstractCollectionTypeDefn> collClass) {
        this(spacePathExpr, MetaType.TYPE, collClass);
    }

    MetaReference(SpacePathExpr spacePathExpr, MetaType targetMetaType, Class<AbstractCollectionTypeDefn> collClass) {
        super(spacePathExpr.getSourceInfo());
        this.spacePathExpr = spacePathExpr;
        this.targetMetaType = targetMetaType;
        this.collClass = collClass;
    }

    public MetaReference(T typeDefn) {
        super(new ProgSourceInfo());
        this.resolvedMetaObj = typeDefn;
        this.state = LoadState.RESOLVED;
    }

    public SpacePathExpr getSpacePathExpr() {
        return spacePathExpr;
    }

    public MetaType getTargetMetaType() {
        return targetMetaType;
    }

    public LoadState getState() {
        return state;
    }

    public void setState(LoadState state) {
        this.state = state;
    }

    public ScopeKind getResolvedDatumScope() {
        return resolvedDatumScope;
    }

    public void setResolvedDatumScope(ScopeKind resolvedDatumScope) {
        this.resolvedDatumScope = resolvedDatumScope;
    }

    public T getResolvedMetaObj() {
        return resolvedMetaObj;
    }

    public void setResolvedMetaObj(T resolvedMetaObj) {
        this.resolvedMetaObj = resolvedMetaObj;
    }

    public Class<AbstractCollectionTypeDefn> getCollClass() {
        return collClass;
    }

    public String getDisplayName() {
        return spacePathExpr.toString() + "(" + (state == LoadState.RESOLVED ? "res" : "unres") + ")";
    }

    @Override
    public String toString() {
        String suffix = getSuffix();
        return "<" +
            "fromObj=" + (getNamedParent() != null ? getNamedParent() : "") +
            " path=" + spacePathExpr + (suffix != null ? " " + suffix : "") +
            " (" + targetMetaType.toString().substring(0, 3) + ")" +
            (resolvedDatumScope != null ? " " + resolvedDatumScope.toString().substring(0, 3) : "") +
            " resObj=" + (resolvedMetaObj != null ? resolvedMetaObj : "?") +
            '>';
    }

    protected String getSuffix() {
        return null;
    }
}
