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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @see ViewDefn
 * @author Jim Coles
 * @version 1.0
 */
public class ViewDefnImpl extends TypeDefnImpl implements ViewDefn {

    //    private Space parentSpace;
    private Set<TypeDefn> providedInterfaces;
    private TypeDefn basisType;
    private List<Expression> elementSequence = new LinkedList<>();
    private List<DatumProjectionExpr> projectionBlockList = new LinkedList<>();

    /** The selection Rule, i.e., the filter. References the projection vars, but does not
     * declare them. */
    private Rule selector;

    ViewDefnImpl(SourceInfo sourceInfo, NamePart namePart, TypeDefn basisType) {
        super(sourceInfo, namePart, true);
        this.basisType = basisType;
    }

    @Override
    public TypeDefn getBasisType() {
        return basisType;
    }

    @Override
    public Set<TypeDefn> getProvidedInterfaces() {
        return providedInterfaces;
    }

    @Override
    public boolean isSimpleIndexDefn() {
        return false;
    }

    @Override
    public boolean isTreeIndexDefn() {
        return false;
    }

    @Override
    public boolean isIndexDefn() {
        return false;
    }

    @Override
    public boolean isKeyDefn() {
        return false;
    }

    @Override
    public boolean isTreeViewDefn() {
        return false;
    }

    @Override
    public boolean isTableViewDefn() {
        return false;
    }

    @Override
    public Rule getSelector() {
        return selector;
    }

    @Override
    public boolean hasPrimaryKey() {
        return basisType.hasPrimaryKey();
    }

    @Override
    public KeyDefnImpl getPrimaryKeyDefn() {
        return null;
    }

    @Override
    public Set<KeyDefnImpl> getAlternateKeyDefns() {
        return null;
    }

    @Override
    public ViewDefn addProjectionDecl(DatumProjectionExpr datumProjectionExpr) {
        elementSequence.add(datumProjectionExpr);
        projectionBlockList.add(datumProjectionExpr);
        //
        addChild(datumProjectionExpr);
        //
        return this;
    }

    @Override
    public List<DatumProjectionExpr> getProjectionDeclList() {
        return projectionBlockList;
    }

}
