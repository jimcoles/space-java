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
import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.Arrays;
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

    public Directory newAstDir(SourceInfo sourceInfo, NamePart namePart) {
        return new Directory(sourceInfo, namePart);
    }

    public Directory newAstDir(String name) {
        return newAstDir(SourceInfo.API, newNamePart(name));
    }

    public Directory newProgram(SourceInfo sourceInfo, NamePart namePart) {
        return new Directory(sourceInfo, namePart);
    }

    public Directory newProgram(String name) {
        return newProgram(SourceInfo.API, newNamePart(name));
    }

    public NamePart newNamePart(SourceInfo sourceInfo, String name) {
        return new NamePart(sourceInfo, name);
    }

    public NamePart newNamePart(String name) {
        return newNamePart(SourceInfo.API, name);
    }

    public TypeDefnImpl newTypeDefn(SourceInfo sourceInfo, NamePart nameNode) {
        TypeDefnImpl complexTypeImpl = new TypeDefnImpl(sourceInfo, nameNode);
        return complexTypeImpl;
    }

    public FunctionDefnImpl newSpaceFunctionDefn(SourceInfo sourceInfo, NamePart namePart, TypeRef returnTypeRef) {
        return new FunctionDefnImpl(sourceInfo, namePart, returnTypeRef);
    }

    public FunctionDefnImpl newSpaceFunctionDefn(String name, TypeRef returnTypeRef) {
        return new FunctionDefnImpl(SourceInfo.API, newNamePart(name), returnTypeRef);
    }

    public VariableDecl newVariableDecl(SourceInfo sourceInfo, NamePart namePart, DatumDeclContext fromDatumContext,
                                        TypeRef typeRef)
    {
        return new VariableDeclImpl(sourceInfo, fromDatumContext, namePart, typeRef);
    }

    public VariableDecl newVariableDecl(String name, DatumDeclContext fromDatumContext, TypeDefn typeDefn) {
        return newVariableDecl(SourceInfo.API, newNamePart(name), fromDatumContext, newTypeRef(typeDefn));
    }

    public FunctionCallExpr newFunctionCallExpr(SourceInfo sourceInfo) {
        return new FunctionCallExpr(sourceInfo);
    }

    public FunctionCallExpr newFunctionCallExpr() {
        return new FunctionCallExpr(SourceInfo.API);
    }

    public AssociationDefnImpl newAssociationDefn(SourceInfo sourceInfo, NamePart namePart)
    {
        return new AssociationDefnImpl(sourceInfo, namePart);
    }

    public FromAssocEndImpl newFromAssocEnd(SourceInfo sourceInfo, AssociationDefn assoc, DatumRef datumRef) {
        return new FromAssocEndImpl(sourceInfo, assoc, datumRef);
    }

    public ToAssociationEndImpl newToAssocEnd(SourceInfo sourceInfo, AssociationDefn assoc, DatumRef datumRef) {
        return new ToAssociationEndImpl(sourceInfo, assoc, datumRef, true, true, 1, 1);
    }

    public TypeDatumRef newTypeDatumRef(SourceInfo sourceInfo, ExpressionChain<DatumDecl> fullPathRef) {
        return new TypeDatumRef(sourceInfo, fullPathRef);
    }

    public TwoPartDatumRef newTwoPartDatumRef(SourceInfo sourceInfo, TypeRef typeRef,
                                           SimpleNameRefExpr<DatumDecl> monoDatumRef) {
        return new TwoPartDatumRef(sourceInfo, typeRef, monoDatumRef);
    }

    public PrimitiveLiteralExpr newPrimLiteralExpr(SourceInfo sourceInfo, NumPrimitiveTypeDefn primitiveTypeDefn,
                                                   String text)
    {
        return new PrimitiveLiteralExpr(sourceInfo, primitiveTypeDefn, text);
    }

    public ValueSequenceLiteralExpr newCharSeqLiteralExpr(SourceInfo sourceInfo, String text) {
        return new ValueSequenceLiteralExpr(sourceInfo,
                                            newTypeRef(NumPrimitiveTypeDefn.CHAR.getSequenceOfType()),
                                            text);
    }

    public ValueExprSequenceExpr newSequenceExpr(SourceInfo sourceInfo, TypeRef containedTypeRef) {
        return new ValueExprSequenceExpr(sourceInfo, containedTypeRef);
    }

    public <T extends Named> SimpleNameRefExpr<T> newNameRefExpr(NamePartExpr namePartExpr) {
        return new SimpleNameRefExpr<>(namePartExpr);
    }


    public <T extends Named> SimpleNameRefExpr<T> newNameRefExpr(SourceInfo sourceInfo, String name) {
        return new SimpleNameRefExpr<>(newNamePartExpr(sourceInfo, null, name));
    }

    public NamePartExpr newNamePartExpr(SourceInfo sourceInfo, PathOperEnum oper, String searchName)
    {
        return new NamePartExpr(sourceInfo, true, oper, searchName);
    }

    /** AST builder API */
    public <T extends Named> SimpleNameRefExpr<T> newNameRefExpr(T namedElement) {
        SimpleNameRefExpr<T> nameRefExpr =
            new SimpleNameRefExpr<>(newNamePartExpr(namedElement.getSourceInfo(), null, namedElement.getName()));
        AstUtils.checkSetResolve(nameRefExpr, namedElement, null);
        return nameRefExpr;
    }

    public ThisTupleExpr newThisExpr(SourceInfo sourceInfo) {
        return new ThisTupleExpr(sourceInfo);
    }

    public ThisTupleExpr newThisExpr() {
        return new ThisTupleExpr(SourceInfo.API);
    }

    public StatementBlock newStatementBlock(SourceInfo sourceInfo) {
        return new StatementBlock(sourceInfo);
    }

    public StatementBlock newStatementBlock() {
        return newStatementBlock(SourceInfo.API);
    }

    public AssignmentExpr newAssignmentExpr(SourceInfo sourceInfo)
    {
        return new AssignmentExpr(sourceInfo);
    }

    public ReturnExpr newReturnExpr(SourceInfo sourceInfo, ValueExpr valueExpr) {
        return new ReturnExpr(sourceInfo, valueExpr);
    }

    public OperatorExpr newOperatorExpr(SourceInfo sourceInfo, Operators.Operator oper, ValueExpr... args) {
        return new OperatorExpr(sourceInfo, oper, args);
    }

    public TupleValueList newTupleValueExprList(SourceInfo sourceInfo) {
        return new TupleValueList(sourceInfo);
    }

    public NewTupleExpr newNewObjectExpr(SourceInfo sourceInfo, TypeRef typeRefPathExpr,
                                         TupleValueList tupleValueList)
    {
        return new NewTupleExpr(sourceInfo, typeRefPathExpr, tupleValueList);
    }

    public NewSetExpr newNewSetExpr(SourceInfo sourceInfo, TypeRef tupleTypeRef) {
        return new NewSetExpr(sourceInfo, tupleTypeRef);
    }

    public ExpressionChain newMetaRefChain(SourceInfo sourceInfo, MetaType metaType, SimpleNameRefExpr nsRefPart) {
        ExpressionChain expressionChain = new ExpressionChain(sourceInfo, metaType);
        expressionChain.setNsRefPart(nsRefPart);
        return expressionChain;
    }

    public ExpressionChain newMetaRefChain(MetaType metaType, String refName) {
        return newMetaRefChain(SourceInfo.API, metaType, newNameRefExpr(SourceInfo.API, refName));
    }

    public void addNewMetaRefParts(ExpressionChain parentPath, SourceInfo sourceInfo, String... nameExprs) {
        for (String nameExpr : nameExprs) {
            parentPath.addNextPart(newNameRefExpr(sourceInfo, nameExpr));
        }
    }

    public TypeRefImpl newTypeRef(SourceInfo sourceInfo, List<TypeRefImpl.CollectionType> collectionTypes,
                                  SimpleNameRefExpr<Namespace> nsRefPart)
    {
        TypeRefImpl typeRef = new TypeRefImpl(sourceInfo, collectionTypes);
        typeRef.setNsRefPart(nsRefPart);
        return typeRef;
    }

    public TypeRefImpl newTypeRef(TypeDefn typeDefn) {
        return new TypeRefImpl(typeDefn);
    }

    public TypeRefImpl newTypeRef(SourceInfo si, TypeDefn typeDefn) {
        return new TypeRefImpl(si, typeDefn);
    }

    public <T extends Named> ExpressionChain<T> newExpressionChain() {
        return new ExpressionChain<>();
    }

    public <T extends Named> AliasedMetaRef<T> newAliasedRefChain(NamePart alias)
    {
        return new AliasedMetaRef<>(SourceInfo.API, alias, newExpressionChain());
    }

    public ParseUnit newParseUnit(SourceInfo sourceInfo) {
        return new ParseUnit(sourceInfo);
    }

    public ParseUnit newParseUnit() {
        return new ParseUnit(SourceInfo.API);
    }

    public PackageDecl newPackageDecl(SourceInfo sourceInfo, ExpressionChain packageRef) {
        return new PackageDecl(sourceInfo, packageRef);
    }

    public Namespace newNamespace(SourceInfo sourceInfo, NamePart namePart, Namespace... nsLookupChain) {
        return new Namespace(sourceInfo, namePart, nsLookupChain);
    }

    public ImportExpr newImportExpr(SourceInfo sourceInfo, TypeRefImpl metaReference, String alias) {
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

    public AssociationDefnImpl newAssociationDecl(String name, FromAssocEnd fromAssocEnd,
                                                  ToAssocEnd toAssocEnd)
    {
        AssociationDefnImpl assocDefn =
            newAssociationDefn(SourceInfo.API, newNamePart(SourceInfo.API, name));
        assocDefn.setFromEnd(fromAssocEnd);
        assocDefn.setToEnd(toAssocEnd);
        return assocDefn;
    }

    public VariableDecl newVariableDecl(NamePart namePart, DatumDeclContext datumDeclContext, TypeDefn simpleTypeDefn) {
        return newVariableDecl(SourceInfo.API, namePart, datumDeclContext, newTypeRef(simpleTypeDefn));
    }

    public KeyDefnImpl newKeyDefn(SourceInfo sourceInfo, TypeDefn basisType, NamePart namePart) {
        return new KeyDefnImpl(sourceInfo, namePart, basisType);
    }

    public KeyDefnImpl newKeyDefn(TypeDefn basisType, String name) {
        return newKeyDefn(SourceInfo.API, basisType, newNamePart(name));
    }

    /**
     * API method
     * @param projectedDatumPath A valid path of datums extending from the view's basis type.
     *                           This is not a list of sibling datums.
     */
    public DatumProjectionExpr newProjectionDecl(ViewDefn container, NamePart namePart, DatumDecl ... projectedDatumPath) {
        AliasedMetaRef<DatumDecl> aliasedRef = newAliasedRefChain(null);
        aliasedRef.addPath(ScopeKind.STATIC, projectedDatumPath);
        DatumDecl leafDatum = projectedDatumPath[projectedDatumPath.length - 1];
        return new DatumProjectionImpl(
            SourceInfo.API, container,
            namePart != null ?
                namePart :
                newNamePart(SourceInfo.COMPOSITION, leafDatum.getName()),
            newTypeRef(leafDatum.getType()),
            aliasedRef
        );
    }

}
