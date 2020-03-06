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

/**
 * A View is an algebraic combination of other Types and/or other Views,
 * both of which are Relations. The Space runtime is aware of Views and their
 * relationship to Entities and manages state values that equate between
 * these elements.
 *
 * <p>A {@link View} can be thought of as a named Query.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class ViewDefn extends NamedElement implements View {

    //    private Space parentSpace;
    private Projection variablesExpr;
    // nestable boolean-valued expression
    private Rule selector;

    public ViewDefn(SourceInfo sourceInfo) {
        super(sourceInfo, null);
    }

    public ViewDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.RULE;
    }

    public Projection getVariablesExpr() {
        return variablesExpr;
    }

    public Rule getSelector() {
        return selector;
    }

}
