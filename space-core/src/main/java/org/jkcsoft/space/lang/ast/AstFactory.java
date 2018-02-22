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

import org.jkcsoft.space.lang.instance.ObjectFactory;

import java.lang.reflect.Method;

/**
 * The AST is built by calling 'new_' methods on this object.  An
 * AstFactory can be used by any AstLoader to aid in the correct formulation
 * of AST objects.
 *
 * (Future) The AST memory model uses Space instance-level constructs to hold the
 * Space program itself, i.e., we are eating our own dogfood.  This gives
 * us the ability to query the AST using relational queries.
 *
 * @author Jim Coles
 */
public class AstFactory {

    public AstFactory() {

    }

    private ObjectFactory getObjectBuilder() {
        return ObjectFactory.getInstance();
    }

    public Schema newAstSchema(SourceInfo sourceInfo, String name) {
        return new Schema(sourceInfo, name);
    }

    public Schema newProgram(SourceInfo sourceInfo, String name) {
        Schema schema = new Schema(sourceInfo, name);
        return schema;
    }

    public SpaceTypeDefn newSpaceTypeDefn(SourceInfo sourceInfo, String name) {
        SpaceTypeDefn spaceTypeDefn = new SpaceTypeDefn(sourceInfo, name);
        return spaceTypeDefn;
    }

    public SpaceActionDefn newSpaceActionDefn(SourceInfo sourceInfo, String name) {
        SpaceActionDefn element = new SpaceActionDefn(sourceInfo, name);
        return element;
    }

    public VariableDefn newVariableDefn(SourceInfo sourceInfo, String name, PrimitiveType type) {
        VariableDefn element = new VariableDefn(sourceInfo, name, type);
        return element;
    }

    public NativeActionDefn newNativeActionDefn(SourceInfo sourceInfo, String name, Method jMethod, SpaceTypeDefn nativeArgSpaceTypeDefn) {
        NativeActionDefn element = new NativeActionDefn(sourceInfo, name, jMethod, nativeArgSpaceTypeDefn);
        return element;
    }

    public ActionCallExpr newActionCallExpr(SourceInfo sourceInfo, SpacePathExpr functionPathExpr, ValueExpr ... argValueExprs) {
        ActionCallExpr element = new ActionCallExpr(sourceInfo, functionPathExpr, argValueExprs);
        return element;
    }

    public AssociationDefn newAssociationDefn(SourceInfo sourceInfo, String name, SpacePathExpr toPath) {
        AssociationDefn element = new AssociationDefn(sourceInfo, name, null, toPath);
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

    public OperLookupExpr newOperLookupExpr(SourceInfo sourceInfo, OperEnum operEnum) {
        OperLookupExpr element = new OperLookupExpr(sourceInfo, operEnum);
        return element;
    }

    public SpacePathExpr newSpacePathExpr(SourceInfo sourceInfo, PathOperEnum oper, String searchName) {
        SpacePathExpr element = new SpacePathExpr(sourceInfo, true, oper, searchName, null);
        return element;
    }

    public ThisExpr newThis() {
        ThisExpr element = new ThisExpr();
        return element;
    }

}
