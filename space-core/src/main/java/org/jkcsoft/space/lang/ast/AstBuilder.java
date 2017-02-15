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
import org.jkcsoft.space.lang.instance.ObjectReference;

import java.lang.reflect.Method;
import java.util.HashMap;
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
    private SpaceProgram astRoot;
    private ModelElement currentAstNode;

    public void addMetaObject(ModelElement object) {
        // metaObjects;
    }

    public SpaceProgram getAstRoot() {
        return astRoot;
    }

    public ModelElement getCurrentAstNode() {
        return currentAstNode;
    }

    public SpaceProgram initProgram() {
        astRoot = new SpaceProgram();
        return  astRoot;
    }

    public SpaceDefn newSpaceDefn(String name) {
        SpaceDefn spaceDefn = new SpaceDefn(name);
        return spaceDefn;
    }

    public SpaceActionDefn newSpaceActionDefn(String name) {
        return new SpaceActionDefn(name);
    }

    public boolean validate() {
        return true;
    }

    private ObjectBuilder getObjectBuilder() {
        return ObjectBuilder.getInstance();
    }

    public CoordinateDefn newCoordinateDefn(String name, PrimitiveType type) {
        return new CoordinateDefn(name, type);
    }

    public AbstractActionDefn newNativeActionDefn(String name, Method jMethod, SpaceDefn nativeArgSpaceDefn) {
        return new NativeActionDefn(name, jMethod, nativeArgSpaceDefn);
    }

    public AssignmentDefn newAssignmentDefn(String leftIdRef, ObjectReference objectReference) {
        return new AssignmentDefn(leftIdRef, objectReference);
    }

    public AbstractActionDefn newCallActionDefn(String functionRefId, AssignmentDefn assignmentDefn) {
        return new CallActionDefn(functionRefId, assignmentDefn);
    }
}
