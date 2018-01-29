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

/**
 * A View is an algebraic combination of other Entities and other Views,
 * both of which are Relations. The Space runtime is aware of Views and their
 * relationship to Entities and manages state values that equate between
 * these elements.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class ViewDefn extends QueryDefn {

    ViewDefn(SpaceTypeDefn contextSpaceTypeDefn, String name) {
        super(contextSpaceTypeDefn, name);
    }

}
