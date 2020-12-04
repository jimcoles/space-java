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

import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * {@link ExpressionChain}'s are used to hold the initial
 * unresolved AST representation of any dot (".") separated expression. After the linking and
 * semantic analysis phases, a valid Expression Chain is then broken into some combination
 * of a MetaRefPath and a ValueExprChain depending on what the syntactic context (possible
 * meta types of the element referenced) and actual type of the resolved element.
 *
 * Holds either:
 *
 * 1. Meta Reference Path: A full reference to a named element via its AST namespace path. The
 *    reference may be multi-part (fully qualified). Holds a chain of {@link SimpleNameRefExpr}s,
 *    each of which resolves to a single meta object. The MetaType of the full reference
 *    should be known at parse time.
 *
 *    Analogous to a Unix symbolic link.
 *
 * 2. Value Expression Chain: A chain of value expressions where link (i + 1) must be a
 *    named member (function or datum) of the TypeDefn associated with link (i). If
 *    link (i) resolves to a value expression (i + 1) must also be a value expression.
 *    Evaluation of a value expression chain is similar to eval of a statement sequence
 *    in a statement block.
 *
 * S.N - NS
 * S.DR - Directory
 * S.T - Type
 * S.DT - (static) Datum, e.g., a constant
 * V.DT - Datum
 * V.F - Func call
 * V.L - Literal
 * V.D - Datum
 *
 * @author Jim Coles
 */
public class ExpressionChain<T extends Named> extends AbstractModelElement implements ValueExpr {

    private MetaType targetMetaType;
    private SimpleNameRefExpr nsRefPart;
    private LinkSource firstExpr;
    private List<NameRefOrHolder> restLinks = new LinkedList<>();
    private LinkedList<LinkSource> allLinks = new LinkedList<>();
    private ScopeKind resolvedDatumScope;  // only relevant if target is a datum type.
    private boolean isImportMatch = false;  // only relevant if this chain is a type ref or other static ref
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;
    private AstLoadError loadError;

    // Redundantly extracted views of the exprLinks:
    private MetaRefPath metaRefPath;
    private ValueExprChain valueExprChain;

    ExpressionChain(SourceInfo sourceInfo, MetaType targetMetaType) {
        super(sourceInfo);
        this.targetMetaType = targetMetaType;
    }

    /** For use by API access when referenced type object is known. */
    ExpressionChain(SourceInfo sourceInfo, TypeDefn typeDefn) {
        this(sourceInfo, MetaType.TYPE);

        SimpleNameRefExpr<TypeDefn> firstPart =
            new SimpleNameRefExpr<>(new NamePartExpr(sourceInfo, true, null, typeDefn.getName()));
        firstPart.setState(LinkState.RESOLVED);
        firstPart.setResolvedMetaObj(typeDefn);
        this.typeCheckState = TypeCheckState.VALID;
        //
        if (firstPart != null) {
            this.addNextPart(firstPart);
        }
    }

    /** For use when referenced type object is known. */
    ExpressionChain(SourceInfo sourceInfo, Declaration datumDecl) {
        this(sourceInfo, MetaType.DATUM);
        SimpleNameRefExpr<Declaration> firstPart =
            new SimpleNameRefExpr<>(new NamePartExpr(sourceInfo, false, null, datumDecl.getName()));
        firstPart.setState(LinkState.RESOLVED);
        firstPart.setResolvedMetaObj(datumDecl);
        this.typeCheckState = TypeCheckState.VALID;
        //
        if (firstPart != null) {
            this.addNextPart(firstPart);
        }
    }

    public ExpressionChain() {
        super(new ProgSourceInfo());
    }

    public boolean isImportRef() {
        return getParent() instanceof ImportExpr;
    }

    public boolean isNamePath() {
        return targetMetaType != null;
    }

    public boolean hasTargetMetaType() {
        return targetMetaType != null;
    }

    public MetaType getTargetMetaType() {
        return targetMetaType;
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

    public SimpleNameRefExpr getNsRefPart() {
        return nsRefPart;
    }

    public ExpressionChain setNsRefPart(SimpleNameRefExpr nsRefPart) {
        this.nsRefPart = nsRefPart;
        return this;
    }

    public List<NameRefOrHolder> getRestLinks() {
        return restLinks;
    }

    public LinkedList<LinkSource> getAllLinks() {
        return allLinks;
    }

    public LinkedList<NameRefOrHolder> getAllLinksAsHolders() {
        if (! (allLinks.getFirst() instanceof NameRefOrHolder))
            throw new IllegalStateException("first element ["+allLinks.getFirst()+"] is not set or is not an identifier");

        LinkedList<NameRefOrHolder> refChain = new LinkedList<>();
        for (LinkSource typedLink : allLinks) {
            refChain.add(((NameRefOrHolder) typedLink));
        }
        return refChain;
    }

    public LinkSource getFirstPart() {
        return firstExpr;
    }

    public ScopeKind getResolvedDatumScope() {
        return resolvedDatumScope;
    }

    public void setResolvedDatumScope(ScopeKind resolvedDatumScope) {
        this.resolvedDatumScope = resolvedDatumScope;
    }

    public T getResolvedMetaObj() {
        return getLastMetaRefLink().getResolvedMetaObj();
    }

    private MetaRef<T> getLastMetaRefLink() {
        return extractMetaRefPath().getLastLink();
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

    protected String getSuffix() {
        return null;
    }

    public LinkSource getLastPart() {
        return allLinks.getLast();
    }

    public int getPathLength() {
        return allLinks.size();
    }

    public void addNextPart(NameRefOrHolder nextPart) {
        if (this.firstExpr == null)
            this.firstExpr = nextPart;
        else
            restLinks.add(nextPart);

        allLinks.add(nextPart);
        //
        addChild(nextPart);
    }

    public boolean isSinglePart() {
        return getPathLength() == 1;
    }

    public boolean isMultiPart() {
        return getPathLength() > 1;
    }

    public LinkState getState() {
        return loadError != null ?
            LinkState.NOT_FOUND :
            (hasRef() ?
                getLastExpr().getNameRef().getRefAsNameRef().getState() :
                LinkState.RESOLVED);
    }


    private LinkSource getLastExpr() {
        return getAllLinks().getLast();
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

    public void setAstLoadError(AstLoadError error) {
        this.loadError = error;
    }

    public AstLoadError getLoadError() {
        return loadError;
    }

    @Override
    public TypeDefn getDatumType() {
        return ((ValueExpr) getLastPart()).getDatumType();
    }

    public boolean hasMetaRefPath() {
        return isStaticRef(firstExpr);
    }

    private boolean isStaticRef(LinkSource linkSource) {
        boolean isStaticRef = false;
        if (linkSource.hasNameRef()
            && linkSource.getNameRef().getRefAsNameRef().isResolved())
        {
            Set<MetaType> staticMetaTypes = Set.of(MetaType.PACKAGE, MetaType.TYPE);
            isStaticRef =
                staticMetaTypes.contains(linkSource.getNameRef().getRefAsNameRef().getResolvedMetaObj().getMetaType());
        }
        return isStaticRef;
    }

    // TODO Add validation: meta path must start at first position
    //      and be contiguous
    public MetaRefPath extractMetaRefPath() {
//        if (!isResolved())
//            throw new IllegalStateException("chain expression not fully resolved");

        if (metaRefPath == null) {
            metaRefPath = new MetaRefPath(this, resolvedDatumScope);
            for (LinkSource refPartExpr : this.allLinks) {
                if (isStaticRef(refPartExpr)) {
                    metaRefPath.addLink(refPartExpr.getNameRef().getRefAsNameRef());
                }
                else
                    break;
            }
        }
        return metaRefPath;
    }

    public boolean hasValueChain() {
        if (!isResolvedValid())
            throw new IllegalStateException("chain expression not fully resolved and validated");

        return getLastPart().getTypedExpr().isValueExpr();
    }

    public ValueExprChain extractValueExprChain() {
        if (!isResolvedValid())
            throw new IllegalStateException("chain expression not fully resolved and validated");

        if (valueExprChain == null) {
            valueExprChain = new ValueExprChain(null);
            for (LinkSource linkExpr : this.allLinks) {
                if (linkExpr.hasTypedExpr() && linkExpr.getTypedExpr().isValueExpr()) {
                    valueExprChain.addValueExpr(((ValueExpr) linkExpr));
                }
            }
        }
        return valueExprChain;
    }

    @Override
    public boolean hasRef() {
        boolean has = false;
        for (LinkSource exprLink : allLinks) {
            if (exprLink.hasNameRef()) {
                has = true;
                break;
            }
        }
        return has;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }

    @Override
    public boolean hasResolvedType() {
        LinkSource lastPart = getLastPart();
        return lastPart.hasTypedExpr() && lastPart.getTypedExpr().hasResolvedType();
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

    public String getFullUrlSpec() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (LinkSource link : this.getAllLinks()) {
            if (link instanceof ByNameMetaRef) {
                String name = ((SimpleNameRefExpr) link).getNameExprText();
                if (isFirst) {
                    isFirst = false;
                }
                else {
                    sb.append(".");
                }
                sb.append(name);
            }
        }
        return sb.toString();
    }

    @Override
    public String getDisplayName() {
        String suffix = getSuffix();
        LinkSource lastPart = getLastPart();
        Named resolvedMetaObj = lastPart.getNameRef().getRefAsNameRef().getResolvedMetaObj();
        return
//            "fromObj=" + (getParent() != null ? getParent() : "") +
            "path=\"" + getFullUrlSpec() + (suffix != null ? " " + suffix : "") + "\"" +
            " mtype=(" + (targetMetaType != null ? targetMetaType.toString().substring(0, 3) : "?") + ")" +
            (resolvedDatumScope != null ? " " + resolvedDatumScope.toString().substring(0, 3) : "") +
            " resObj=" + (getState() == LinkState.RESOLVED ? resolvedMetaObj.toString() : "?");

    }
}
