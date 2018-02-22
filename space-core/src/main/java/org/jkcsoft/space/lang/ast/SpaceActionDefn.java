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

/**
 * An SpaceActionDefn is a composite sequence of imperative nestedActions.  An
 * SpaceActionDefn:
 * <ul>
 * <li>may be named or not (anonymous)
 * <li>may be callable (function) or not (nested)</li>
 * <li>may be automatically derived from an Equation (future feature)
 * <li>may be associated by the user with an Equation in which case the
 * Space designtime or runtime checks that the Equation is always true.
 *</ul>
 * @author Jim Coles
 */
public class SpaceActionDefn extends AbstractActionDefn implements Callable {

    private SpaceTypeDefn argSpaceTypeDefn;
    private SpaceTypeDefn localSpaceTypeDefn;
    private List<ActionCallExpr> nestedActions = new LinkedList<>();  // child statements

    SpaceActionDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    public void setArgSpaceTypeDefn(SpaceTypeDefn argSpaceTypeDefn) {
        this.argSpaceTypeDefn = argSpaceTypeDefn;
        argSpaceTypeDefn.setContextSpaceTypeDefn(this.getContextSpaceTypeDefn());
    }

    @Override
    public SpaceTypeDefn getArgSpaceTypeDefn() {
        return argSpaceTypeDefn;
    }

    public ActionCallExpr addAction(ActionCallExpr nestedActionDefn) {
        nestedActions.add(nestedActionDefn);
        return nestedActionDefn;
    }

    public List<ActionCallExpr> getNestedActions() {
        return nestedActions;
    }

    public ActionCallExpr addAssignment(ActionCallExpr actionCallExpr) {
        return null;
    }
}
