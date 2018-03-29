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

    public FunctionDefn newSpaceFunctionDefn(SourceInfo sourceInfo, String name, SpacePathExpr returnPathExpr) {
        FunctionDefn element = new FunctionDefn(sourceInfo, name, returnPathExpr);
        return element;
    }

    public VariableDefn newVariableDefn(SourceInfo sourceInfo, String name, PrimitiveTypeDefn type) {
        VariableDefn element = new VariableDefn(sourceInfo, name, type);
        return element;
    }

    public NativeFunctionDefn newNativeFunctionDefn(SourceInfo sourceInfo, String name, Method jMethod,
                                                    SpaceTypeDefn nativeArgSpaceTypeDefn,
                                                    SpacePathExpr returnPathExpr)
    {
        NativeFunctionDefn element = new NativeFunctionDefn(sourceInfo, name, jMethod, nativeArgSpaceTypeDefn,
                                                            returnPathExpr);
        return element;
    }

    public FunctionCallExpr newFunctionCallExpr(SourceInfo sourceInfo) {
        FunctionCallExpr element = new FunctionCallExpr(sourceInfo);
        return element;
    }

    public AssociationDefn newAssociationDefn(SourceInfo sourceInfo, String name, SpacePathExpr toPath) {
        AssociationDefn element = new AssociationDefn(sourceInfo, name, null, toPath);
        return element;
    }

    public PrimitiveLiteralExpr newPrimLiteralExpr(SourceInfo sourceInfo, PrimitiveTypeDefn primitiveTypeDefn, String text) {
        PrimitiveLiteralExpr element = new PrimitiveLiteralExpr(sourceInfo, primitiveTypeDefn, text);
        return element;
    }

    public SequenceLiteralExpr newSequenceLiteralExpr(SourceInfo sourceInfo, String text) {
        SequenceLiteralExpr element =
            new SequenceLiteralExpr(sourceInfo, newSpacePathExpr(sourceInfo, null, "CharSequence", null), text);
        return element;
    }

    public SequenceLiteralExpr newSequenceLiteralExpr(SourceInfo sourceInfo, SpacePathExpr pathToType, String text) {
        SequenceLiteralExpr element = new SequenceLiteralExpr(sourceInfo, pathToType, text);
        return element;
    }

    public SpacePathExpr newSpacePathExpr(SourceInfo sourceInfo, PathOperEnum oper, String searchName,
                                          SpacePathExpr nextExpr)
    {
        SpacePathExpr element = new SpacePathExpr(sourceInfo, true, oper, searchName, nextExpr);
        return element;
    }

    public ThisExpr newThisExpr(SourceInfo sourceInfo) {
        ThisExpr element = new ThisExpr(sourceInfo);
        return element;
    }

    public SpaceTypeDefnBody newTypeDefnBody(SourceInfo codeSourceInfo) {
        return new SpaceTypeDefnBody(codeSourceInfo);
    }

    public StatementBlock newStatementBlock(SourceInfo sourceInfo) {
        return new StatementBlock(sourceInfo);
    }

    public AssignmentExpr newAssignmentExpr(SourceInfo sourceInfo)
    {
        AssignmentExpr element = new AssignmentExpr(sourceInfo);
        return element;
    }

    public ReturnExpr newReturnExpr(SourceInfo sourceInfo, ValueExpr valueExpr) {
        return new ReturnExpr(sourceInfo, valueExpr);
    }

    public StreamTypeDefn newStreamTypeDefn(IntrinsicSourceInfo sourceInfo, String name) {
        return new StreamTypeDefn(sourceInfo, name);
    }

    public OperatorExpr newOperatorExpr(SourceInfo sourceInfo, OperEnum oper, ValueExpr... args) {
        return new OperatorExpr(sourceInfo, oper, args);
    }

}
