/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.ModelElement;

/**
 * A base class for all object representing an instance of a meta object.
 *
 * @author Jim Coles
 */
public class AbstractExeContext implements ExeContext {

    private ModelElement astNode;

    public AbstractExeContext(ModelElement astNode) {
        this.astNode = astNode;
    }

    public ModelElement getAstNode() {
        return astNode;
    }
}
