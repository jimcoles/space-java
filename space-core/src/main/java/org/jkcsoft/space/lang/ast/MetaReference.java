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
 * Top-level notion for holding a full reference to a named element. The reference may be
 * multi-part (fully qualified).  Holds a chain of MetaRefPart's,
 * each of which resolves to a single meta object.  The MetaType of the full reference
 * should be known at parse time.
 *
 * @param <T> The class of meta object being referenced.
 * @author Jim Coles
 */
public class MetaReference<T extends Named> extends ModelElement implements ValueExpr {

    private MetaType targetMetaType;
    private MetaRefPart firstPart;

    private ScopeKind resolvedDatumScope;  // only relevant if target is a datum type.
    private Class<AbstractCollectionTypeDefn> collClass;
    private MetaRefPart<T> lastPart;
    private LoadState state = LoadState.INITIALIZED;

    MetaReference(SourceInfo sourceInfo, MetaType targetMetaType) {
        this(sourceInfo, targetMetaType, null);
    }

    MetaReference(SourceInfo sourceInfo, Class<AbstractCollectionTypeDefn> collClass) {
        this(sourceInfo, MetaType.TYPE, collClass);
    }

    MetaReference(SourceInfo sourceInfo, MetaType targetMetaType, Class<AbstractCollectionTypeDefn> collClass) {
        super(sourceInfo);
        this.firstPart = firstPart;
        this.targetMetaType = targetMetaType;
        this.collClass = collClass;
    }

    public MetaReference(T typeDefn) {
        this(new IntrinsicSourceInfo(), MetaType.TYPE, null);
        this.firstPart = new MetaRefPart(this, new NamePartExpr(new IntrinsicSourceInfo(), false, null, typeDefn.getName()));
        this.firstPart.setState(LoadState.RESOLVED);
        this.firstPart.setResolvedMetaObj(typeDefn);
    }

    public void setFirstPart(MetaRefPart firstPart) {
        this.firstPart = firstPart;
    }

    public MetaRefPart getFirstPart() {
        return firstPart;
    }

    public MetaType getTargetMetaType() {
        return targetMetaType;
    }

    public ScopeKind getResolvedDatumScope() {
        return resolvedDatumScope;
    }

    public void setResolvedDatumScope(ScopeKind resolvedDatumScope) {
        this.resolvedDatumScope = resolvedDatumScope;
    }

    public T getResolvedMetaObj() {
        return getLastPart().getResolvedMetaObj();
    }

    public Class<AbstractCollectionTypeDefn> getCollClass() {
        return collClass;
    }

//    public String getDisplayName() {
//        return getLastPart().toString() + "(" + (getLastPart().getState() == LoadState.RESOLVED ? "res" : "unres") + ")";
//    }

    public String getFullPath() {
        return "->" + firstPart.getName() + (firstPart.getNextRefPart() != null ? firstPart.getFullNamePath(): "");
    }

    @Override
    public String getDisplayName() {
        return getFullPath();
    }

    protected String getSuffix() {
        return null;
    }

    private MetaRefPart<T> getLastPart() {
        if (lastPart == null) {
            lastPart = firstPart;
            while (lastPart.getNextRefPart() != null) {
                lastPart = lastPart.getNextRefPart();
            }
        }
        return lastPart;
    }

    public LoadState getState() {
        return this.state;
    }

    public void setState(LoadState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        String suffix = getSuffix();
        MetaRefPart<T> lastPart = getLastPart();
        return "<" +
            "fromObj=" + (getNamedParent() != null ? getNamedParent() : "") +
            " path=\"" + getFullPath() + (suffix != null ? " " + suffix : "") + "\"" +
            " (" + ( targetMetaType != null ? targetMetaType.toString().substring(0, 3) : "?" ) + ")" +
            (resolvedDatumScope != null ? " " + resolvedDatumScope.toString().substring(0, 3) : "") +
            " resObj=" + (getState() == LoadState.RESOLVED ? lastPart.getResolvedMetaObj() : "?") +
            '>';
    }
}
