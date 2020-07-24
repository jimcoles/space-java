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

import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Used to define the variable set of a {@link ViewDefn} with respect to a set of
 * {@link TypeDefn}'s. Projection Declarations may contain sub-structures that make it
 * convenient for programs to define sub-graphs that navigate multiple associations.
 *
 * @author Jim Coles
 */
public class ProjectionDeclImpl extends NamedElement implements ProjectionDecl {

    private ProjectionDecl parentProjectionDecl;

    /** May be a base type or a view. */
    private AliasedMetaRef<TypeDefn> basisTypeRef;
    /**
     * May reference associations or variables.
     */
    private List<AliasedMetaRef> typeAssocs;
    private Comparator<Tuple> datumComparator;

//    private TypeContext rootTypeContext;

    protected ProjectionDeclImpl(SourceInfo sourceInfo, String name, AliasedMetaRef ... typeAssocs) {
        super(sourceInfo, name);
        this.typeAssocs = Arrays.asList(typeAssocs);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public AliasedMetaRef<TypeDefn> getBasisTypeRef() {
        return basisTypeRef;
    }

    @Override
    public AliasedMetaRef getTypeGraphRef() {
        return typeAssocs.get(0);
    }

    @Override
    public ProjectionDecl addChildProjection(ProjectionDecl proj) {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return basisTypeRef.getMetaRefExpr().getResolvedMetaObj();
    }

    @Override
    public boolean isAssoc() {
        return false;
    }

    @Override
    public Comparator<Tuple> getDatumComparator() {
        if (datumComparator == null)
            datumComparator = AstUtils.buildDatumComparator(this);
        return datumComparator;
    }
}
