/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
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
 * The concrete implementation of a {@link DatumProjectionExpr}.
 *
 * @author Jim Coles
 */
public class DatumProjectionImpl extends AbstractDatumDecl implements DatumProjectionExpr {

    /** The context may be a {@link ViewDefn} or a parent {@link DatumProjectionExpr}. */
    private ProjectionContext parentContext;
    private AliasedMetaRef<DatumDecl> projectedDatumRef;

    /** May reference 1-m datum paths wrt the parent type or parent projection scope. */
    private List<DatumProjectionImpl> children;
    private Comparators.DatumTupleComparator datumComparator;

    protected DatumProjectionImpl(SourceInfo sourceInfo, ViewDefn parentContext, NamePart namePart, TypeRef typeRef,
                                  AliasedMetaRef<DatumDecl> projectedDatumRef)
    {
        super(sourceInfo, parentContext, namePart, typeRef);
        this.parentContext = parentContext;
        this.projectedDatumRef = projectedDatumRef;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public TypeDefn getBasisType() {
        return parentContext.getBasisType();
    }

    @Override
    public DatumDecl getProjectedDatum() {
        return this.projectedDatumRef.getMetaRefExpr().getResolvedMetaObj();
    }

    @Override
    public TypeDefn getType() {
        return projectedDatumRef.getMetaRefExpr().getDatumType();
    }

    @Override
    public boolean hasAssoc() {
        return false;
    }

    @Override
    public Comparators.DatumTupleComparator getDatumComparator() {
        if (datumComparator == null)
            datumComparator = Comparators.buildDatumComparator(this);
        return datumComparator;
    }
}
