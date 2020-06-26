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

import java.util.Set;

/**
 *
 * @see ViewDefn
 * @author Jim Coles
 * @version 1.0
 */
public class ViewDefnImpl extends AbstractTypeDefn implements ViewDefn {

    //    private Space parentSpace;
    private ProjectionDecl variablesExpr;
    // nestable boolean-valued expression
    private Rule selector;

    public ViewDefnImpl(SourceInfo sourceInfo) {
        super(sourceInfo, null);
    }

    public ViewDefnImpl(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.RULE;
    }

    @Override
    public ProjectionDecl getVariableProjection() {
        return variablesExpr;
    }

    @Override
    public Rule getSelector() {
        return selector;
    }

    @Override
    public KeyDefn getPrimaryKeyDefn() {
        return null;
    }

    @Override
    public Set<KeyDefn> getAllKeyDefns() {
        return null;
    }
}
