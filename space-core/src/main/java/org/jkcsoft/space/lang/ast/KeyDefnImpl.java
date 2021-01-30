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
 * We're extending {@link ViewDefnImpl} because a Key is just a list of variables.
 *
 * @author Jim Coles
 */
public class KeyDefnImpl extends NamedElement implements KeyDefn {

    private TypeDefn basisTypeDefn;
    private List<ProjectionDecl> projectionDeclList = new LinkedList<>();

    protected KeyDefnImpl(SourceInfo sourceInfo, String name, TypeDefn basisTypeDefn, ProjectionDecl ... projectionVars) {
        super(sourceInfo, name);
        this.basisTypeDefn = basisTypeDefn;
        for (ProjectionDecl projectionDecl : projectionVars) {
            addProjectionDecl(projectionDecl);
        }
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    @Override
    public TypeDefn getBasisTypeDefn() {
        return basisTypeDefn;
    }

    private void addProjectionDecl(ProjectionDecl projectionDecl) {
        projectionDeclList.add(projectionDecl);
    }

    @Override
    public List<ProjectionDecl> getProjectionDeclList() {
        return projectionDeclList;
    }
}
