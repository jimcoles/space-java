/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr.g1.g1;

import org.jkcsoft.space.lang.ast.AstFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class Im2AstTransform {

    private AstFactory astFactory = new AstFactory();
    private List<AstLoadError> errors = new LinkedList<>();

    public void transform(ImTreeNode imRoot) {
        if (astFactory.getAstRoot() == null) {
            if (!ImTrees.first(imRoot).getText().equals("space-defn"))
                errors.add(new AstLoadError("first ID must be 'space-defn'"));
//            astFactory.addMetaObject(new SpaceProgram());
            astFactory.getAstRoot();
        }
    }

}