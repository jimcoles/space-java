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

import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.LinkedList;
import java.util.List;

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
public class MetaReference<T extends NamedElement> extends ModelElement implements ValueExpr {

    private MetaType targetMetaType;
    private MetaRefPart<Namespace> nsRefPart;
    private List<MetaRefPart> pathParts = new LinkedList<>();
//    private MetaRefPart firstPart;
    // redundant lazy init fields ...
//    private MetaRefPart<T> lastPart;
//    private int pathLength;

    private ScopeKind resolvedDatumScope;  // only relevant if target is a datum type.
    private boolean isImportMatch = false;
    private Class<AbstractCollectionTypeDefn> collClass;
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;

//    private LinkState state = null;

    MetaReference(SourceInfo sourceInfo, MetaType targetMetaType, Class<AbstractCollectionTypeDefn> collClass)
    {
        super(sourceInfo);
        this.targetMetaType = targetMetaType;
        this.collClass = collClass;
    }

    MetaReference(SourceInfo sourceInfo, MetaType targetMetaType) {
        this(sourceInfo, targetMetaType, null);
    }

    MetaReference(SourceInfo sourceInfo, Class<AbstractCollectionTypeDefn> collClass) {
        this(sourceInfo, MetaType.TYPE, collClass);
    }

    public MetaReference(SourceInfo sourceInfo, T typeDefn) {
        this(sourceInfo, MetaType.TYPE, null);
        MetaRefPart<T> firstPart =
            new MetaRefPart(new NamePartExpr(sourceInfo, false, null, typeDefn.getName()));
        firstPart.setState(LinkState.RESOLVED);
        firstPart.setResolvedMetaObj(typeDefn);
        this.typeCheckState = TypeCheckState.VALID;
        //
        pathParts.add(firstPart);
    }

    public MetaReference() {
        super(new ProgSourceInfo());
    }

    public TypeCheckState getTypeCheckState() {
        return typeCheckState;
    }

    public void setTypeCheckState(TypeCheckState typeCheckState) {
        this.typeCheckState = typeCheckState;
    }

    public boolean hasNs() {
        return nsRefPart != null;
    }

    public MetaRefPart<Namespace> getNsRefPart() {
        return nsRefPart;
    }

    public MetaReference<T> setNsRefPart(MetaRefPart<Namespace> nsRefPart) {
        this.nsRefPart = nsRefPart;
        return this;
    }

    public List<MetaRefPart> getPathParts() {
        return pathParts;
    }

    public MetaRefPart getFirstPart() {
        return pathParts.get(0);
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

//    public String getDisplayName() {
//        return getLastPart().toString() + "(" + (getLastPart().getState() == LoadState.RESOLVED ? "res" : "unres") + ")";
//    }

    public Class<AbstractCollectionTypeDefn> getCollClass() {
        return collClass;
    }

    public String getFullUrlSpec() {
        return (nsRefPart != null ? nsRefPart.getNamePartExpr().getNameExpr() + ":" : "")
            + getUrlPathSpec();
    }

    public String getUrlPathSpec() {
        return Strings.buildDelList(pathParts, (Lister<MetaRefPart>) obj -> obj.getNamePartExpr().getNameExpr(), ".");
    }

    @Override
    public String getDisplayName() {
        return getUrlPathSpec();
    }

    protected String getSuffix() {
        return null;
    }

    public MetaRefPart<T> getLastPart() {
        return pathParts.get(pathParts.size() - 1);
    }

    public int getPathLength() {
        return pathParts.size();
    }

    public void addNextPart(MetaRefPart nextPart) {
        pathParts.add(nextPart);
    }

    public boolean isSinglePart() {
        return getPathLength() == 1;
    }

    public boolean isMultiPart() {
        return getPathLength() > 1;
    }

    public LinkState getState() {
        return getLastPart().getState();
    }

    public boolean isAtInitState() {
        return getState() == LinkState.INITIALIZED;
    }

    public boolean isResolved() {
        return getState() == LinkState.RESOLVED;
    }

    public boolean isResolvedValid() {
        return getState() == LinkState.RESOLVED && this.typeCheckState == TypeCheckState.VALID;
    }

    @Override
    public String toString() {
        String suffix = getSuffix();
        MetaRefPart<T> lastPart = getLastPart();
        return "<" +
            "fromObj=" + (getParent() != null ? getParent() : "") +
            " path=\"" + getFullUrlSpec() + (suffix != null ? " " + suffix : "") + "\"" +
            " (" + (targetMetaType != null ? targetMetaType.toString().substring(0, 3) : "?") + ")" +
            (resolvedDatumScope != null ? " " + resolvedDatumScope.toString().substring(0, 3) : "") +
            " resObj=" + (getState() == LinkState.RESOLVED ? lastPart.getResolvedMetaObj() : "?") +
            '>';

    }
}
