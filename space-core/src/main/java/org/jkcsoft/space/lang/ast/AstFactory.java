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

    public SpaceTypeDefn newSpaceTypeDefn(SourceInfo sourceInfo, NamePart nameNode) {
        SpaceTypeDefn spaceTypeDefn = new SpaceTypeDefn(sourceInfo, nameNode);
        return spaceTypeDefn;
    }


    public SpaceFunctionDefn newSpaceFunctionDefn(SourceInfo sourceInfo, String name, TypeRefImpl returnTypeRef) {
        SpaceFunctionDefn element = new SpaceFunctionDefn(sourceInfo, name, returnTypeRef);
        return element;
    }

    public VariableDeclImpl newVariableDecl(SourceInfo sourceInfo, String name, NumPrimitiveTypeDefn type) {
        VariableDeclImpl element = new VariableDeclImpl(sourceInfo, name, type);
        return element;
    }

    public FunctionCallExpr newFunctionCallExpr(SourceInfo sourceInfo) {
        FunctionCallExpr element = new FunctionCallExpr(sourceInfo);
        return element;
    }

    public AssociationDeclImpl newAssociationDecl(SourceInfo sourceInfo, String name, TypeRef toTypeRef) {
        AssociationDeclImpl element = new AssociationDeclImpl(sourceInfo, name, null, toTypeRef);
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

    public MetaRefPart newMetaRefPart(MetaReference parentPath, SourceInfo sourceInfo, String... nameExprs) {
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

    public NewObjectExpr newNewObjectExpr(SourceInfo sourceInfo, TypeRefImpl typeRefPathExpr, TupleExpr tupleExpr) {
        return new NewObjectExpr(sourceInfo, typeRefPathExpr, tupleExpr);
    }

    public NewSetExpr newNewSetExpr(SourceInfo sourceInfo, TypeRefImpl tupleTypeRef) {
        return new NewSetExpr(sourceInfo, tupleTypeRef);
    }

    public MetaReference newMetaReference(SourceInfo sourceInfo, MetaType type, MetaRefPart nsRefPart) {
        MetaReference metaReference = new MetaReference(sourceInfo, type, nsRefPart);
        return metaReference;
    }

    public TypeRefImpl newTypeRef(SourceInfo sourceInfo, List<TypeRefImpl.CollectionType> collectionTypes,
                                  MetaRefPart nsRefPart)
    {
        return new TypeRefImpl(sourceInfo, collectionTypes, nsRefPart);
    }

    public TypeRefImpl newTypeRef(DatumType typeDefn) {
        return new TypeRefImpl(typeDefn);
    }

    public ParseUnit newParseUnit(SourceInfo sourceInfo) {
        return new ParseUnit(sourceInfo);
    }

    public PackageDecl newPackageDecl(SourceInfo sourceInfo, MetaReference<Directory> packageRef) {
        return new PackageDecl(sourceInfo, packageRef);
    }

    public ParsableChoice newParsableChoice(Directory directory) {
        return new ParsableChoice(directory);
    }

    public ParsableChoice newParsableChoice(ParseUnit parseUnit) {
        return new ParsableChoice(parseUnit);
    }

    public Namespace newNamespace(SourceInfo sourceInfo, String name) {
        return new Namespace(sourceInfo, name);
    }

    public ImportExpr newImportExpr(SourceInfo sourceInfo, TypeRefImpl metaReference, String alias) {
        return new ImportExpr(sourceInfo, metaReference, alias);
    }
}
