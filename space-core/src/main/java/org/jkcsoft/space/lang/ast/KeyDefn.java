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

/**
 * A general means of defining primary keys, alternate keys, and external keys.
 * Keys may be a simple a a single, opaque byte or as complex as tuple of scalar
 * variables.
 *
 * 4GL: Defining a Key at the language-level pushes Space in the 4GL direction.
 *
 * Keys enable some nice high-level things:
 * <li>Language-managed Indexes and Trees.
 * <li>Mapping to key-oriented database models.
 * <li>CDI-like injection of objects, i.e., Space should feel like it has an intrinsic
 * CDI injector.
 *
 * @author Jim Coles
 */
public class KeyDefn {

    private Projection vars;

    public Projection getVars() {
        return vars;
    }
}
