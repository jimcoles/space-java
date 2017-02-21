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

import org.jkcsoft.space.util.Namespace;

/**
 * A character-based identifier for source code definable things such as
 * Spaces, Coordinates, and Functions.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class IdentifierRefDefn implements AssignableDefn {

    private Namespace path;

    IdentifierRefDefn(Namespace path) {
        this.path = path;
    }

    public Namespace getPath() {
        return path;
    }
}
