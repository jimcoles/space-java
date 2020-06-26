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
 * Used to define the variable set of a {@link ViewDefn} with respect to a set of
 * {@link TypeDefn}'s. Projection Declarations may contain sub-structures that make it
 * convenient for programs to define deep associations.
 *
 * @author Jim Coles
 */
public class ProjectionDeclImpl extends NamedElement implements ProjectionDecl {

    private ProjectionDecl parentProjectionDecl;

    /** May be a base type or a view. */
    private TypeDefn rootTypeDefn;
    /**
     * Must reference types of associations. Used by projection and rule.
     */
    private List<AliasedMetaRef> typeAssocs;
    /**
     * Variables or Associations relative to root type or aliased associations.
     */
    private List<ExpressionChain> relativeDatumPaths;

//    private TypeContext rootTypeContext;

    protected ProjectionDeclImpl(SourceInfo sourceInfo, String name, List<AliasedMetaRef> typeAssocs) {
        super(sourceInfo, name);
        this.typeAssocs = typeAssocs;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public TypeDefn getRootType() {
        return rootTypeDefn;
    }

    @Override
    public AliasedMetaRef getTypeGraphRef() {
        return null;
    }

    @Override
    public ProjectionDecl addChild(ProjectionDecl proj) {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return rootTypeDefn;
    }
}
