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

import org.jkcsoft.space.lang.instance.ObjectBuilder;
import org.jkcsoft.space.lang.instance.SpaceObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The AST is built by calling adder methods on this object.
 * The AST memory model uses Space instance-level constructs to hold the
 * Space program itself, i.e., we are eating our own dogfood.  This gives
 * us the ability to query the AST using relational queries.
 *
 * @author Jim Coles
 */
public class AstBuilder {

    // holds things (mostly named things) defined in the source code
    private Map<String, Class<ModelElement>> metaTypeMap = new HashMap<>();
    public SpaceProgram astRoot;
    private ModelElement currentAstNode;

    private ObjectBuilder getObjectBuilder() {
        return ObjectBuilder.getInstance();
    }

    public void addMetaObject(ModelElement object) {
        // metaObjects;
    }

    public SpaceProgram getAstRoot() {
        return astRoot;
    }

    public ModelElement getCurrentAstNode() {
        return currentAstNode;
    }

    public SpaceProgram addRoot() {
        astRoot = new SpaceProgram();
        return  astRoot;
    }

    public boolean validate() {
        return true;
    }


}
