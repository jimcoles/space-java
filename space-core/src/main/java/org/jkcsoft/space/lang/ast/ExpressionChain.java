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
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * {@link ExpressionChain}'s are the initial unresolved AST representation of any
 * dot (".") separated expression. After the linking and
 * semantic analysis phases, a valid Expression Chain is then broken into some combination
 * of a MetaRefPath and a ValueExprChain depending on the syntactic context (possible
 * meta types of the element referenced) and actual type of the resolved elements
 * in the raw Expression Chain.
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

    private SimpleNameRefExpr<Namespace> nsRefPart;
    // The primary chain representation: a list of LinkSource's
    private final LinkedList<LinkSource> allLinks = new LinkedList<>();
    //
    // Redundantly extracted views of the exprLinks:
    private List<NameRefOrHolder> restLinks = new LinkedList<>();
    private MetaRefPath metaRefPath;
    private ValueExprChain valueExprChain;
    //
    private MetaType targetMetaType; // some chains have a known target meta type based on syntax
    private LinkSource firstExpr;
    private boolean isImportMatch = false;  // only relevant if this chain is a type ref or other static ref
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;
    private AstLoadError loadError;

    public ExpressionChain() {
        super(SourceInfo.API);
    }

    ExpressionChain(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    ExpressionChain(SourceInfo sourceInfo, MetaType targetMetaType) {
        super(sourceInfo);
        this.targetMetaType = targetMetaType;
    }

    /** For use via AST builder API when referenced type object is known. */
    ExpressionChain(TypeDefn typeDefn) {
        this(SourceInfo.API, typeDefn);
    }

    ExpressionChain(SourceInfo sourceInfo, TypeDefn typeDefn) {
        this(sourceInfo, MetaType.TYPE);

        SimpleNameRefExpr<TypeDefn> firstPart =
            new SimpleNameRefExpr<>(new NamePartExpr(SourceInfo.INTRINSIC, true, null, typeDefn.getName()));
        AstUtils.checkSetResolve(firstPart, typeDefn, null);
        this.typeCheckState = TypeCheckState.VALID;
        //
        this.addNextPart(firstPart);
    }

    /** For use via AST builder API when referenced type object is known. */
    ExpressionChain(DatumDecl datumDecl, ScopeKind scopeKind) {
        this(SourceInfo.API, MetaType.DATUM);
        SimpleNameRefExpr<DatumDecl> firstPart =
            new SimpleNameRefExpr<>(new NamePartExpr(SourceInfo.API, false, null, datumDecl.getName()));
        AstUtils.checkSetResolve(firstPart, datumDecl, scopeKind);
        this.typeCheckState = TypeCheckState.VALID;
        //
        this.addNextPart(firstPart);
    }

    public boolean hasNs() {
        return nsRefPart != null;
    }

    public ExpressionChain<T> setNsRefPart(SimpleNameRefExpr<Namespace> nsRefPart) {
        this.nsRefPart = nsRefPart;
        return this;
    }

    public SimpleNameRefExpr<Namespace> getNsRefPart() {
        return nsRefPart;
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

    /** AST builder API */
    public void addPath(ScopeKind firstRefScopeKind, DatumDecl ... datumPath) {
        AstFactory ast = AstFactory.getInstance();
        boolean first = true;
        for (DatumDecl datumDecl : datumPath) {
            SimpleNameRefExpr<DatumDecl> pathPart = ast.newNameRefExpr(
                new NamePartExpr(SourceInfo.API, false, null, datumDecl.getName())
            );
            AstUtils.checkSetResolve(pathPart, datumDecl, first ? firstRefScopeKind : ScopeKind.OBJECT);
            addNextPart(pathPart);
            first = false;
        }
        setTypeCheckState(TypeCheckState.VALID);
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

    public T getResolvedMetaObj() {
        // full chain = [meta ref path].[value chain]
        if (!isResolved())
            throw new IllegalStateException("chain expression not fully resolved");
        return (T) getLastPart().getNameRef().getRefAsNameRef().getResolvedMetaObj();
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

        if (firstExpr.isLiteral()) {
            return null;
        }
        if (metaRefPath == null) {
            metaRefPath = new MetaRefPath(this);
            for (LinkSource linkExpr : this.allLinks) {
//                if (isStaticRef(linkExpr)) {
                    metaRefPath.addLink(linkExpr.getNameRef().getRefAsNameRef());
//                }
//                else
//                    break;
            }
        }
        return metaRefPath;
    }

    public boolean hasValueChain() {
        if (!isResolvedValid())
            throw new IllegalStateException("chain expression not fully resolved and validated");

        LinkSource lastPart = getLastPart();
        return lastPart.isValueExpr();
    }

    public ValueExprChain extractValueExprChain() {
        if (!isResolvedValid())
            throw new IllegalStateException("chain expression not fully resolved and validated");

        if (valueExprChain == null) {
            valueExprChain = new ValueExprChain(null);
            for (LinkSource linkExpr : this.allLinks) {
                if (linkExpr.isValueExpr()) {
                    valueExprChain.addValueExpr(linkExpr.getValueExpr());
                }
                else
                    if (valueExprChain.size() > 0)
                        break;
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
        return lastPart.isValueExpr() && lastPart.getValueExpr().hasResolvedType();
    }

    @Override
    public boolean isValueExpr() {
        return getLastExpr().isValueExpr();
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
            " resObj=" + (getState() == LinkState.RESOLVED ? resolvedMetaObj.toString() : "?");
    }

    @Override
    public boolean isLiteral() {
        return allLinks.size() == 1 && firstExpr.isLiteral();
    }
}
