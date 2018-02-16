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

import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.instance.ObjectFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
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
public class AstFactory {

    // holds things (mostly named things) defined in the source code
    private Map<String, Class<ModelElement>> metaTypeMap = new HashMap<>();
    private SpaceProgram astRoot;
    private ModelElement currentAstNode;

    public AstFactory() {

    }

    private ObjectFactory getObjectBuilder() {
        return ObjectFactory.getInstance();
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

    public SpaceProgram initProgram(String name) {
        astRoot = new SpaceProgram(name);
        currentAstNode = astRoot;
        return  astRoot;
    }

    public SpaceTypeDefn newSpaceTypeDefn(String name) {
        SpaceTypeDefn spaceTypeDefn = new SpaceTypeDefn(name);
        return spaceTypeDefn;
    }

    public SpaceActionDefn newSpaceActionDefn(String name) {
        SpaceActionDefn element = new SpaceActionDefn(name);
        return element;
    }

    public VariableDefn newVariableDefn(String name, PrimitiveType type) {
        VariableDefn element = new VariableDefn(name, type);
        return element;
    }

    public AbstractActionDefn newNativeActionDefn(String name, Method jMethod, SpaceTypeDefn nativeArgSpaceTypeDefn) {
        NativeActionDefn element = new NativeActionDefn(name, jMethod, nativeArgSpaceTypeDefn);
        return element;
    }

    public ActionCallExpr newActionCallExpr(String name, SpacePathExpr functionPathExpr, ValueExpr ... argValueExprs) {
        ActionCallExpr element = new ActionCallExpr(name, functionPathExpr, argValueExprs);
        return element;
    }

    public AssociationDefn newAssociationDefn(String name, SpacePathExpr toPath) {
        AssociationDefn element = new AssociationDefn(name, null, toPath);
        return element;
    }

    public LiteralExpr newLiteralHolder(String text) {
        LiteralExpr element = new LiteralExpr(text);
        return element;
    }

    public MetaObjectRefLiteral newMetaObjectRefLiteral(ModelElement spaceMetaObject) {
        MetaObjectRefLiteral element = new MetaObjectRefLiteral(spaceMetaObject);
        return element;
    }

    public OperLookupExpr newOperLookupExpr(OperEnum operEnum) {
        OperLookupExpr element = new OperLookupExpr(operEnum);
        return element;
    }

    public SpacePathExpr newSpacePathExpr(PathOperEnum oper, String searchName) {
        SpacePathExpr element = new SpacePathExpr(true, oper, searchName, null);
        return element;
    }

    public ThisExpr newThis() {
        ThisExpr element = new ThisExpr();
        return element;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        ModelElement modelElement = this.getAstRoot();
        append(sb, modelElement, 0);
        return sb.toString();
    }

    private void append(StringBuilder sb, ModelElement modelElement, int depth) {
        String indent = Strings.multiplyString("\t", depth);
        sb.append(JavaHelper.EOL)
                .append(indent)
                .append("(")
                .append(modelElement.toString());
        List<ModelElement> children = modelElement.getChildren();
        for (ModelElement child : children) {
            append(sb, child, depth + 1);
        }
        sb.append(JavaHelper.EOL)
                .append(indent)
                .append(")");
    }

}
