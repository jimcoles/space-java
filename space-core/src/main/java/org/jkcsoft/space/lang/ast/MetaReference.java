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

import java.util.Iterator;

/**
 * Top-level notion for holding a full reference to a named element. The reference may be
 * multi-part (fully qualified).  Holds a chain of MetaRefPart's,
 * each of which resolves to a single meta object.  The MetaType of the full reference
 * should be known at parse time.
 *
 * Analogous to a Unix symbolic link.
 *
 * @param <T> The class of meta object being referenced.
 * @author Jim Coles
 */
public class MetaReference<T extends Named> extends ModelElement implements ValueExpr {

    private MetaType targetMetaType;
    private MetaRefPart<Namespace> nsRefPart;
    private MetaRefPart firstPart;
    // redundant lazy init fields ...
    private MetaRefPart<T> lastPart;
    private int pathLength;

    private ScopeKind resolvedDatumScope;  // only relevant if target is a datum type.
    private boolean isImportMatch = false;
    private Class<AbstractCollectionTypeDefn> collClass;

    private LoadState state = LoadState.INITIALIZED;

    MetaReference(SourceInfo sourceInfo, MetaType targetMetaType, MetaRefPart<Namespace> nsRefPart,
                  Class<AbstractCollectionTypeDefn> collClass)
    {
        super(sourceInfo);
        this.nsRefPart = nsRefPart;
        this.targetMetaType = targetMetaType;
        this.collClass = collClass;
    }

    MetaReference(SourceInfo sourceInfo, MetaType targetMetaType, MetaRefPart<Namespace> nsRefPart) {
        this(sourceInfo, targetMetaType, nsRefPart, null);
    }

    MetaReference(SourceInfo sourceInfo, Class<AbstractCollectionTypeDefn> collClass) {
        this(sourceInfo, MetaType.TYPE, null, collClass);
    }

    public MetaReference(T typeDefn) {
        this(new IntrinsicSourceInfo(), MetaType.TYPE, null, null);
        this.firstPart =
            new MetaRefPart(this, new NamePartExpr(new IntrinsicSourceInfo(), false, null, typeDefn.getName()));
        this.firstPart.setState(LoadState.RESOLVED);
        this.firstPart.setResolvedMetaObj(typeDefn);
    }

    public MetaReference() {
        super(new ProgSourceInfo());
    }

    public MetaRefPart<Namespace> getNsRefPart() {
        return nsRefPart;
    }

    public MetaRefPart getFirstPart() {
        return firstPart;
    }

    public void setFirstPart(MetaRefPart firstPart) {
        this.firstPart = firstPart;
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

    public boolean isImportMatch() {
        return isImportMatch;
    }

    public void setImportMatch(boolean importMatch) {
        isImportMatch = importMatch;
    }

    public Class<AbstractCollectionTypeDefn> getCollClass() {
        return collClass;
    }

//    public String getDisplayName() {
//        return getLastPart().toString() + "(" + (getLastPart().getState() == LoadState.RESOLVED ? "res" : "unres") + ")";
//    }

    public String getFullPath() {
        return ( nsRefPart != null ? nsRefPart.getName() + ":" : "")
            + firstPart.getName()
            + (firstPart.getNextRefPart() != null ? firstPart.getFullNamePath() : "");
    }

    @Override
    public String getDisplayName() {
        return getFullPath();
    }

    protected String getSuffix() {
        return null;
    }

    public MetaRefPart<T> getLastPart() {
        if (lastPart == null) {
            lastPart = firstPart;
            while (lastPart.getNextRefPart() != null) {
                lastPart = lastPart.getNextRefPart();
            }
        }
        return lastPart;
    }

    public int getPathLength() {
        if (pathLength == 0) {
            getPartIterable().forEach(part -> pathLength++);
        }
        return pathLength;
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
            " (" + (targetMetaType != null ? targetMetaType.toString().substring(0, 3) : "?") + ")" +
            (resolvedDatumScope != null ? " " + resolvedDatumScope.toString().substring(0, 3) : "") +
            " resObj=" + (getState() == LoadState.RESOLVED ? lastPart.getResolvedMetaObj() : "?") +
            '>';
    }

    public Iterable<MetaRefPart> getPartIterable() {
        return () -> new MetaRefPartIterator();
    }

    public boolean isUQ() {
        return getPathLength() == 1;
    }

    private class MetaRefPartIterator implements Iterator<MetaRefPart> {
        MetaRefPart currentPart = firstPart;

        @Override
        public boolean hasNext() {
            return currentPart.hasNextExpr();
        }

        @Override
        public MetaRefPart next() {
            MetaRefPart nextRefPart = currentPart.getNextRefPart();
            currentPart = nextRefPart;
            return nextRefPart;
        }

    }
}
