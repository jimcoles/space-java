/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

    private SpaceDefn argSpaceDefn;
    private List<AbstractActionDefn> nestedActions = new LinkedList<>();  // child nestedActions

    SpaceActionDefn(String name) {
        super(name);
    }

    public void setArgSpaceDefn(SpaceDefn argSpaceDefn) {
        this.argSpaceDefn = argSpaceDefn;
        argSpaceDefn.setContextSpaceDefn(this.getContextSpaceDefn());
    }

    @Override
    public SpaceDefn getArgSpaceDefn() {
        return argSpaceDefn;
    }

    public AbstractActionDefn addAction(AbstractActionDefn nestedActionDefn) {
        nestedActions.add(nestedActionDefn);
        return nestedActionDefn;
    }

    public List<AbstractActionDefn> getNestedActions() {
        return nestedActions;
    }
}
