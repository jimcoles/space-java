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

import java.util.Set;

/**
 *
 * @see ViewDefn
 * @author Jim Coles
 * @version 1.0
 */
public class ViewDefnImpl extends TypeDefnImpl implements ViewDefn {

    //    private Space parentSpace;
    // nestable boolean-valued expression
    private TypeDefn basisType;
    private Rule selector;

    ViewDefnImpl(SourceInfo sourceInfo, NamePart namePart, TypeDefn basisType) {
        super(sourceInfo, namePart, true);
        this.basisType = basisType;
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
    public TypeDefn getBasisType() {
        return basisType;
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
}
