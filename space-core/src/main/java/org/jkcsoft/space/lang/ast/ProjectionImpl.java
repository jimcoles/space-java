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

import java.util.List;

/**
 * Used to define the variable set of a {@link View} with respect to a set of
 * {@link ComplexType}'s.
 *
 * @author Jim Coles
 */
public class ProjectionImpl extends AbstractProjection implements Projection {

    /**
     * Must reference types of associations. Used by projection and rule.
     */
    private List<AliasedMetaRef> typeAssocs;
    /**
     * Variables relative to root type or aliased associations.
     */
    private List<ExpressionChain> varPaths;

//    private TypeContext rootTypeContext;

    protected ProjectionImpl(SourceInfo sourceInfo, String name, List<AliasedMetaRef> typeAssocs) {
        super(sourceInfo, name);
        this.typeAssocs = typeAssocs;
    }

    @Override
    public ComplexType getRootType() {
        return ((ComplexType) typeAssocs.get(0).getTypeOrAssocRef().getResolvedMetaObj());
    }

}
