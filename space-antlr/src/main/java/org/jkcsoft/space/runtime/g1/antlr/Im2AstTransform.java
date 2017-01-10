/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.runtime.g1.antlr;

import org.jkcsoft.space.lang.ast.AstBuilder;

/**
 * @author Jim Coles
 */
public class Im2AstTransform {

    private AstBuilder astBuilder = new AstBuilder();

    public void transform(ImTreeNode imRoot) {
        if (astBuilder.getAstRoot() == null) {
            // TODO much ...
        }
    }
}