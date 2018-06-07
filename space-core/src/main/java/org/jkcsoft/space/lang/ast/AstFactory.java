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
import java.util.Collections;
import java.util.List;

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

    public NamePart newTextNode(SourceInfo sourceInfo, String name) {
        return new NamePart(sourceInfo, name);
    }

    public SpaceTypeDefn newSpaceTypeDefn(SourceInfo sourceInfo, NamePart nameNode) {
        SpaceTypeDefn spaceTypeDefn = new SpaceTypeDefn(sourceInfo, nameNode);
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
        return new SequenceLiteralExpr(sourceInfo, newTypeRef(NumPrimitiveTypeDefn.CHAR), text);
    }

    public MetaRefPart newMetaRefPart(MetaReference parentPath, NamePartExpr namePartExpr) {
        return new MetaRefPart(parentPath, namePartExpr);
    }

    public MetaRefPart newMetaRefPart(MetaReference parentPath, SourceInfo sourceInfo, String ... nameExprs) {
        MetaRefPart firstMetaRefPart = null;
        MetaRefPart prevMetaRefPart = null;
        for (String nameExpr : nameExprs) {
            MetaRefPart metaRefPart = new MetaRefPart(parentPath, newNamePartExpr(sourceInfo, null, nameExpr));
            if (firstMetaRefPart == null)
                firstMetaRefPart = metaRefPart;
            if (prevMetaRefPart != null) {
                prevMetaRefPart.setNextRefPart(metaRefPart);
                prevMetaRefPart = metaRefPart;
            }
        }
        return firstMetaRefPart;
    }

    public NamePartExpr newNamePartExpr(SourceInfo sourceInfo, PathOperEnum oper, String searchName)
    {
        return new NamePartExpr(sourceInfo, true, oper, searchName);
    }

    public ThisTupleExpr newThisExpr(SourceInfo sourceInfo) {
        return new ThisTupleExpr(sourceInfo);
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

    public NewObjectExpr newNewObjectExpr(SourceInfo sourceInfo, TypeRef typeRefPathExpr, TupleExpr tupleExpr) {
        return new NewObjectExpr(sourceInfo, typeRefPathExpr, tupleExpr);
    }

    public NewSetExpr newNewSetExpr(SourceInfo sourceInfo, TypeRef tupleTypeRef) {
        return new NewSetExpr(sourceInfo, tupleTypeRef);
    }

    public MetaReference newMetaReference(SourceInfo sourceInfo, MetaType type) {
        MetaReference metaReference = new MetaReference(sourceInfo, type);
        return metaReference;
    }

    public TypeRef newTypeRef(SourceInfo sourceInfo, List<TypeRef.CollectionType> collectionTypes) {
        return new TypeRef(sourceInfo, collectionTypes);
    }

    public TypeRef newTypeRef(DatumType typeDefn) {
        return new TypeRef(typeDefn);
    }

    public ParseUnit newParseUnit(SourceInfo sourceInfo) {
        return new ParseUnit(sourceInfo);
    }

    public PackageDecl newPackageDecl(SourceInfo sourceInfo, MetaReference<Schema> packageRef) {
        return new PackageDecl(sourceInfo, packageRef);
    }

    public ParsableChoice newParsableChoice(Schema schema) {
        return new ParsableChoice(schema);
    }

    public ParsableChoice newParsableChoice(ParseUnit parseUnit) {
        return new ParsableChoice(parseUnit);
    }
}
