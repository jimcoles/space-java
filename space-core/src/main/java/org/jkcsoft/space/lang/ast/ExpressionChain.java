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

import java.util.LinkedList;
import java.util.List;

/**
 * Very much like a LISP "List Expression". Expression Chains are used to hold the initial
 * AST representation of any dot (".") separated expression. After the linking and
 * semantic analysis phases, a valid Expression Chain is then broken into some combination
 * of a MetaRefPath and a ValueExprChain.
 *
 * Holds either:
 *
 * 1. Meta Reference Path: A full reference to a named element via its AST name path. The
 *    reference may be multi-part (fully qualified). Holds a chain of {@link NameRefExpr}s,
 *    each of which resolves to a single meta object. The MetaType of the full reference
 *    should be known at parse time.
 *
 *    Analogous to a Unix symbolic link.
 *
 * 2. Value Expression Chain: A chain of value expressions where link (i + 1) must be a
 *    named member (function or datum) of the DatumType associated with link (i). If
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
public class ExpressionChain extends AbstractModelElement implements ValueExpr {

    private MetaType targetMetaType;
    private NameRefExpr nsRefPart;
    private TypedExpr firstExpr;
    private List<MemberRefHolder> restLinks = new LinkedList<>();
    private LinkedList<TypedExpr> allLinks = new LinkedList<>();
    private ScopeKind resolvedDatumScope;  // only relevant if target is a datum type.
    private boolean isImportMatch = false;  // only relevant if this chain is a type ref or other static ref
    private TypeCheckState typeCheckState = TypeCheckState.UNCHECKED;

    // Redundantly extracted views of the exprLinks:
    private MetaRefPath metaRefPath;
    private ValueExprChain valueExprChain;

    ExpressionChain(SourceInfo sourceInfo, MetaType targetMetaType) {
        super(sourceInfo);
        this.targetMetaType = targetMetaType;
    }

    ExpressionChain(SourceInfo sourceInfo, DatumType typeDefn) {
        this(sourceInfo, MetaType.TYPE);

        NameRefExpr<DatumType> firstPart =
            new NameRefExpr<>(new NamePartExpr(sourceInfo, false, null, typeDefn.getName()));
        firstPart.setState(LinkState.RESOLVED);
        firstPart.setResolvedMetaObj(typeDefn);
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

    public TypeCheckState getTypeCheckState() {
        return typeCheckState;
    }

    public void setTypeCheckState(TypeCheckState typeCheckState) {
        this.typeCheckState = typeCheckState;
    }

    public boolean hasNs() {
        return nsRefPart != null;
    }

    public NameRefExpr getNsRefPart() {
        return nsRefPart;
    }

    public ExpressionChain setNsRefPart(NameRefExpr nsRefPart) {
        this.nsRefPart = nsRefPart;
        return this;
    }

    public List<MemberRefHolder> getRestLinks() {
        return restLinks;
    }

    public LinkedList<TypedExpr> getAllLinks() {
        return allLinks;
    }

    public LinkedList<MemberRefHolder> getAllLinksAsHolders() {
        if (! (allLinks.getFirst() instanceof MemberRefHolder))
            throw new IllegalStateException("first element ["+allLinks.getFirst()+"] is not set or is not an identifier");

        LinkedList<MemberRefHolder> refChain = new LinkedList<>();
        for (TypedExpr typedLink : allLinks) {
            refChain.add(((MemberRefHolder) typedLink));
        }
        return refChain;
    }

    public TypedExpr getFirstPart() {
        return firstExpr;
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

    public Named getResolvedMetaObj() {
        return getLastMetaRefLink().getResolvedMetaObj();
    }

    private MetaRef getLastMetaRefLink() {
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

    public TypedExpr getLastPart() {
        return allLinks.getLast();
    }

    public int getPathLength() {
        return allLinks.size();
    }

    public void addNextPart(MemberRefHolder nextPart) {
        if (this.firstExpr == null)
            this.firstExpr = nextPart;
        else
            restLinks.add(nextPart);

        allLinks.add(nextPart);
    }

    public boolean isSinglePart() {
        return getPathLength() == 1;
    }

    public boolean isMultiPart() {
        return getPathLength() > 1;
    }

    public LinkState getState() {
        return hasRef() ? getLastRef().getRef().getState() : LinkState.RESOLVED;
    }

    private TypedExpr getLastRef() {
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

    @Override
    public DatumType getDatumType() {
        return ((ValueExpr) getLastPart()).getDatumType();
    }

    public boolean hasMetaRefPath() {
        if (!isResolvedValid())
            throw new IllegalStateException("chain expression not fully resolved and validated");

        return !getFirstPart().isValueExpr();
    }

    // TODO Add validation: meta path must start at first position
    //      and be contiguous
    public MetaRefPath extractMetaRefPath() {
//        if (!isResolved())
//            throw new IllegalStateException("chain expression not fully resolved");

        if (metaRefPath == null) {
            metaRefPath = new MetaRefPath(this, resolvedDatumScope);
            for (TypedExpr refPartExpr : this.allLinks) {
                if (refPartExpr.hasRef()) {
                    metaRefPath.addLink(refPartExpr.getRef());
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

        return getLastPart().isValueExpr();
    }

    public ValueExprChain extractValueExprChain() {
        if (!isResolvedValid())
            throw new IllegalStateException("chain expression not fully resolved and validated");

        if (valueExprChain == null) {
            valueExprChain = new ValueExprChain(null);
            for (TypedExpr linkExpr : this.allLinks) {
                if (linkExpr.isValueExpr()) {
                    valueExprChain.addValueExpr(((ValueExpr) linkExpr));
                }
            }
        }
        return valueExprChain;
    }

    @Override
    public boolean hasRef() {
        boolean has = false;
        for (TypedExpr exprLink : allLinks) {
            if (exprLink.hasRef()) {
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
        return getLastPart().hasResolvedType();
    }

    @Override
    public boolean isValueExpr() {
        return false;
    }

    public String getFullUrlSpec() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (TypedExpr link : this.getAllLinks()) {
            if (link instanceof ByNameMetaRef) {
                String name = ((NameRefExpr) link).getNameExprText();
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
    public String toString() {
        String suffix = getSuffix();
        Linkable lastPart = getLastPart();
        return "<" +
            "fromObj=" + (getParent() != null ? getParent() : "") +
            " path=\"" + getFullUrlSpec() + (suffix != null ? " " + suffix : "") + "\"" +
            " (" + (targetMetaType != null ? targetMetaType.toString().substring(0, 3) : "?") + ")" +
            (resolvedDatumScope != null ? " " + resolvedDatumScope.toString().substring(0, 3) : "") +
            " resObj=" + (getState() == LinkState.RESOLVED ? lastPart : "?") +
            '>';

    }
}
