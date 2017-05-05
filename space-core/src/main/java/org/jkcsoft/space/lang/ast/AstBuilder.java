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
import org.jkcsoft.space.util.Namespace;

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

    private ObjectBuilder getObjectBuilder() {
        return ObjectBuilder.getInstance();
    }

    public boolean validate() {
        return true;
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

    public VariableDefn newCoordinateDefn(String name, PrimitiveType type) {
        return new VariableDefn(name, type);
    }

    public AbstractActionDefn newNativeActionDefn(String name, Method jMethod, SpaceDefn nativeArgSpaceDefn) {
        return new NativeActionDefn(name, jMethod, nativeArgSpaceDefn);
    }

    public ActionCallExpr newActionCallExpr(String name, ValueExpr functionPathExpr, ValueExpr ... argValueExprs) {
        return new ActionCallExpr(name, functionPathExpr, argValueExprs);
    }

    public AssociationDefn newAssociationDefn(String name, SpaceDefn from, SpaceDefn to) {
        return new AssociationDefn(name, from, to);
    }

    public LiteralExpr newLiteralHolder(String text) {
        return new LiteralExpr(text);
    }

    public MetaObjectRefLiteral newMetaObjectRefLiteral(ModelElement spaceMetaObject) {
        return new MetaObjectRefLiteral(spaceMetaObject);
    }

    public OperLookupExpr newOperLookupExpr(OperEnum operEnum) {
        return new OperLookupExpr(operEnum);
    }

    public SpacePathExpr newSpacePathExpr() {
        return new SpacePathExpr();
    }

    public ThisExpr newThis() {
        return new ThisExpr();
    }

}
