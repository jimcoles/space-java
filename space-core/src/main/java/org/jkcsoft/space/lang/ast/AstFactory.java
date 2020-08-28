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

    public Directory newAstDir(SourceInfo sourceInfo, String name) {
        return new Directory(sourceInfo, name);
    }

    public Directory newProgram(SourceInfo sourceInfo, String name) {
        Directory directory = new Directory(sourceInfo, name);
        return directory;
    }

    public NamePart newNamePart(SourceInfo sourceInfo, String name) {
        return new NamePart(sourceInfo, name);
    }

    public TypeDefnImpl newTypeDefn(SourceInfo sourceInfo, NamePart nameNode) {
        TypeDefnImpl complexTypeImpl = new TypeDefnImpl(sourceInfo, nameNode);
        return complexTypeImpl;
    }

    public SpaceFunctionDefn newSpaceFunctionDefn(SourceInfo sourceInfo, String name, FullTypeRefImpl returnTypeRef) {
        SpaceFunctionDefn element = new SpaceFunctionDefn(sourceInfo, name, returnTypeRef);
        return element;
    }

    public VariableDeclImpl newVariableDecl(SourceInfo sourceInfo, String name, TypeRef typeRef) {
        VariableDeclImpl element = new VariableDeclImpl(sourceInfo, name, typeRef);
        return element;
    }

    public FunctionCallExpr newFunctionCallExpr(SourceInfo sourceInfo) {
        FunctionCallExpr element = new FunctionCallExpr(sourceInfo);
        return element;
    }

    public AssociationDefnImpl newAssociationDecl(SourceInfo sourceInfo, String name, TypeRef fromTypeRef,
                                                  TypeRef toTypeRef)
    {
        AssociationDefnImpl element = new AssociationDefnImpl(sourceInfo, name, fromTypeRef, toTypeRef);
        return element;
    }

    public PrimitiveLiteralExpr newPrimLiteralExpr(SourceInfo sourceInfo, NumPrimitiveTypeDefn primitiveTypeDefn,
                                                   String text)
    {
        PrimitiveLiteralExpr element = new PrimitiveLiteralExpr(sourceInfo, primitiveTypeDefn, text);
        return element;
    }

    public SequenceLiteralExpr newCharSeqLiteralExpr(SourceInfo sourceInfo, String text) {
        return new SequenceLiteralExpr(sourceInfo, newTypeRef(sourceInfo, NumPrimitiveTypeDefn.CHAR), text);
    }

    public SimpleNameRefExpr newNameRefExpr(NamePartExpr namePartExpr) {
        return new SimpleNameRefExpr(namePartExpr);
    }

    public SimpleNameRefExpr newNameRefExpr(SourceInfo sourceInfo, String nameExpr) {
        return new SimpleNameRefExpr(newNamePartExpr(sourceInfo, null, nameExpr));
    }

    public NamePartExpr newNamePartExpr(SourceInfo sourceInfo, PathOperEnum oper, String searchName)
    {
        return new NamePartExpr(sourceInfo, true, oper, searchName);
    }

    public ThisTupleExpr newThisExpr(SourceInfo sourceInfo) {
        return new ThisTupleExpr(sourceInfo);
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

    public OperatorExpr newOperatorExpr(SourceInfo sourceInfo, Operators.Operator oper, ValueExpr... args) {
        return new OperatorExpr(sourceInfo, oper, args);
    }

    public TupleValueList newTupleExpr(SourceInfo sourceInfo) {
        return new TupleValueList(sourceInfo);
    }

    public NewTupleExpr newNewObjectExpr(SourceInfo sourceInfo, FullTypeRefImpl typeRefPathExpr,
                                         TupleValueList tupleValueList)
    {
        return new NewTupleExpr(sourceInfo, typeRefPathExpr, tupleValueList);
    }

    public NewSetExpr newNewSetExpr(SourceInfo sourceInfo, FullTypeRefImpl tupleTypeRef) {
        return new NewSetExpr(sourceInfo, tupleTypeRef);
    }

    public ExpressionChain newMetaRefChain(SourceInfo sourceInfo, MetaType type, SimpleNameRefExpr nsRefPart) {
        ExpressionChain expressionChain = new ExpressionChain(sourceInfo, type);
        expressionChain.setNsRefPart(nsRefPart);
        return expressionChain;
    }

    public FullTypeRefImpl newTypeRef(SourceInfo sourceInfo, List<FullTypeRefImpl.CollectionType> collectionTypes,
                                      SimpleNameRefExpr nsRefPart)
    {
        FullTypeRefImpl typeRef = new FullTypeRefImpl(sourceInfo, collectionTypes);
        typeRef.setNsRefPart(nsRefPart);
        return typeRef;
    }

    public FullTypeRefImpl newTypeRef(SourceInfo sourceInfo, TypeDefn typeDefn) {
        return new FullTypeRefImpl(sourceInfo, typeDefn);
    }

    public AliasedMetaRef newMetaRefChain(SourceInfo sourceInfo, String name, Declaration datumDecl) {
        return new AliasedMetaRef(sourceInfo, name, new ExpressionChain(sourceInfo, datumDecl));
    }

    public ParseUnit newParseUnit(SourceInfo sourceInfo) {
        return new ParseUnit(sourceInfo);
    }

    public PackageDecl newPackageDecl(SourceInfo sourceInfo, ExpressionChain packageRef) {
        return new PackageDecl(sourceInfo, packageRef);
    }

    public ParsableChoice newParsableChoice(Directory directory) {
        return new ParsableChoice(directory);
    }

    public ParsableChoice newParsableChoice(ParseUnit parseUnit) {
        return new ParsableChoice(parseUnit);
    }

    public Namespace newNamespace(SourceInfo sourceInfo, String name, Namespace... nsLookupChain) {
        return new Namespace(sourceInfo, name, nsLookupChain);
    }

    public ImportExpr newImportExpr(SourceInfo sourceInfo, FullTypeRefImpl metaReference, String alias) {
        return new ImportExpr(sourceInfo, metaReference, alias);
    }

    public ValueExprChain newValueExprChain(SourceInfo sourceInfo) {
        return new ValueExprChain(sourceInfo);
    }

    public IntrinsicContainer newIntrinsicContainer() {
        return new IntrinsicContainer(SourceInfo.INTRINSIC);
    }

    public TypeDefnImpl newTypeDefn(String typeName) {
        return newTypeDefn(SourceInfo.API, newNamePart(SourceInfo.API, typeName));
    }

    public AssociationDefn newAssociationDecl(String assocName, TypeDefn fromTypeDef, TypeDefn toTypeDef) {
        return newAssociationDecl(SourceInfo.API, assocName,
                                  newTypeRef(SourceInfo.API, fromTypeDef),
                                  newTypeRef(SourceInfo.API, toTypeDef));
    }

    public VariableDecl newVariableDecl(String varName, TypeDefn simpleTypeDefn) {
        return newVariableDecl(SourceInfo.API, varName, newTypeRef(SourceInfo.API, simpleTypeDefn));
    }

    public KeyDefnImpl newKeyDefn(TypeDefn basisType, ProjectionDecl ... vars) {
        return new KeyDefnImpl(SourceInfo.API, newNamePart(SourceInfo.API, "primaryKey"), basisType, vars);
    }

    /** API method */
    public ProjectionDecl newProjectionDecl(String name, Declaration ... datumDecls) {
        AliasedMetaRef varExprs[] = new AliasedMetaRef[datumDecls.length];
        int idx = 0;
        for (Declaration datumDecl : datumDecls) {
            varExprs[idx] = newMetaRefChain(SourceInfo.API, null, datumDecl);
            idx++;
        }
        return new ProjectionDeclImpl(SourceInfo.API, name, varExprs);
    }
}
