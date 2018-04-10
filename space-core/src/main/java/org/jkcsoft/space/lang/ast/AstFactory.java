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
import org.jkcsoft.space.lang.metameta.MetaType;

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

    private static final AstFactory instance = new AstFactory();

    public static AstFactory getInstance() {
        return instance;
    }

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

    public FunctionDefn newSpaceFunctionDefn(SourceInfo sourceInfo, String name, TypeRef returnTypeRef) {
        FunctionDefn element = new FunctionDefn(sourceInfo, name, returnTypeRef);
        return element;
    }

    public VariableDecl newVariableDecl(SourceInfo sourceInfo, String name, NumPrimitiveTypeDefn type) {
        VariableDecl element = new VariableDecl(sourceInfo, name, type);
        return element;
    }

    public NativeFunctionDefn newNativeFunctionDefn(SourceInfo sourceInfo, String name, Method jMethod,
                                                    SpaceTypeDefn nativeArgSpaceTypeDefn,
                                                    TypeRef returnTypeRef)
    {
        NativeFunctionDefn element = new NativeFunctionDefn(sourceInfo, name, jMethod, nativeArgSpaceTypeDefn,
                                                            returnTypeRef);
        return element;
    }

    public FunctionCallExpr newFunctionCallExpr(SourceInfo sourceInfo) {
        FunctionCallExpr element = new FunctionCallExpr(sourceInfo);
        return element;
    }

    public AssociationDecl newAssociationDecl(SourceInfo sourceInfo, String name, TypeRef toTypeRef) {
        AssociationDecl element = new AssociationDecl(sourceInfo, name, null, toTypeRef);
        return element;
    }

    public PrimitiveLiteralExpr newPrimLiteralExpr(SourceInfo sourceInfo, NumPrimitiveTypeDefn primitiveTypeDefn,
                                                   String text)
    {
        PrimitiveLiteralExpr element = new PrimitiveLiteralExpr(sourceInfo, primitiveTypeDefn, text);
        return element;
    }

    public SequenceLiteralExpr newCharSeqLiteralExpr(SourceInfo sourceInfo, String text) {
        SequenceLiteralExpr element = new SequenceLiteralExpr(sourceInfo, newTypeRef(
            newSpacePathExpr(sourceInfo, null, "char", null), TypeRef.CollectionType.SEQUENCE), text);
        return element;
    }

    public SpacePathExpr newSpacePathExpr(SourceInfo sourceInfo, PathOperEnum oper, String searchName,
                                          SpacePathExpr nextExpr)
    {
        SpacePathExpr element = new SpacePathExpr(sourceInfo, true, oper, searchName, nextExpr);
        return element;
    }

    public ThisTupleExpr newThisExpr(SourceInfo sourceInfo) {
        ThisTupleExpr element = new ThisTupleExpr(sourceInfo);
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

    public OperatorExpr newOperatorExpr(SourceInfo sourceInfo, OperEnum oper, ValueExpr... args) {
        return new OperatorExpr(sourceInfo, oper, args);
    }

    public TupleExpr newTupleExpr(SourceInfo sourceInfo) {
        return new TupleExpr(sourceInfo);
    }

    public NewObjectExpr newNewObjectExpr(SourceInfo sourceInfo, SpacePathExpr typeRefPathExpr, TupleExpr tupleExpr) {
        return new NewObjectExpr(sourceInfo, typeRefPathExpr, tupleExpr);
    }

    public NewSetExpr newNewSetExpr(SourceInfo sourceInfo, SpacePathExpr tupleTypeRef) {
        return new NewSetExpr(sourceInfo, tupleTypeRef);
    }

    public MetaReference newMetaReference(SpacePathExpr spacePathExpr, MetaType type) {
        return new MetaReference(spacePathExpr, type);
    }

    public TypeRef newTypeRef(SpacePathExpr spacePathExpr, TypeRef.CollectionType collectionType) {
        return new TypeRef(spacePathExpr, collectionType);
    }

    public TypeRef newTypeRef(SpacePathExpr spacePathExpr) {
        return new TypeRef(spacePathExpr);
    }

    public TypeRef newTypeRef(DatumType typeDefn) {
        return new TypeRef(typeDefn);
    }

    public TypeRef newTypeRef(DatumType typeDefn, TypeRef.CollectionType collectionType) {
        return new TypeRef(typeDefn, collectionType);
    }

}
