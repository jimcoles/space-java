/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.antlr.loaders;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.ExecState;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.io.File;
import java.util.*;

/**
 * A brute force interrogator of the ANTLR parse tree that I'll use
 * until or unless I think of a more elegant approach.  The scheme here is that
 * a top-level toAst( ) method takes an ANTLR parse tree to build top-level
 * AST notions, then recurses into child notions.
 *
 * @author Jim Coles
 */
public class Antlr2AstTransform {

    private static final Logger log = LoggerFactory.getLogger(Antlr2AstTransform.class);
    // -------------------------------------------------------------------------
    //
    private Map<Class<? extends ParseTree>, Transformer> transformerMap = new HashMap<>();
    private ExecState state = ExecState.INITIALIZED;
    private List<AstLoadError> errors = new LinkedList();
    private AstFactory astFactory;
    private File srcFile;
    private Map<String, Operators.Operator> operSymbolMap = new TreeMap();
    private int countParseNodes = 0;

//    private ObjectFactory objBuilder = ObjectFactory.getInstance();

    {
        operSymbolMap.put("+", Operators.IntAlgOper.ADD);
        operSymbolMap.put("-", Operators.IntAlgOper.SUB);
        operSymbolMap.put("*", Operators.IntAlgOper.MULT);
        operSymbolMap.put("/", Operators.IntAlgOper.DIV);

        operSymbolMap.put("&", Operators.BoolOper.AND);
        operSymbolMap.put("&&", Operators.BoolOper.COND_AND);
        operSymbolMap.put("|", Operators.BoolOper.OR);
        operSymbolMap.put("||", Operators.BoolOper.COND_OR);
        operSymbolMap.put("!", Operators.BoolOper.NEGATION);

        operSymbolMap.put("==", Operators.IntCompOper.EQ);
        operSymbolMap.put("<", Operators.IntCompOper.LT);
        operSymbolMap.put(">", Operators.IntCompOper.GT);
    }

    public Antlr2AstTransform(AstFactory astFactory, File srcFile) {
        this.astFactory = astFactory;
        this.srcFile = srcFile;
    }

    public ParseUnit toAst(SpaceParser.ParseUnitContext spaceParseUnit) {
        log.info("transforming ANTLR parse tree to AST starting with root parse node.");
        logTrans(spaceParseUnit);
        ParseUnit parseUnitAST = astFactory.newParseUnit(toSI(spaceParseUnit));
        List<SpaceParser.SpaceTypeDefnContext> spaceTypeDefnCtxts = spaceParseUnit.spaceTypeDefn();
        SpaceParser.PackageStatementContext packageStatementCtxt = spaceParseUnit.packageStatement();
        if (packageStatementCtxt != null)
            parseUnitAST.setPackageDecl(toAst(packageStatementCtxt));

        List<SpaceParser.ImportStatementContext> importStatementCtxts = spaceParseUnit.importStatement();
        for (SpaceParser.ImportStatementContext importStatementCtxt : importStatementCtxts) {
            parseUnitAST.addImportExpr(toAst(importStatementCtxt));
        }

        for (SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext : spaceTypeDefnCtxts) {
            parseUnitAST.addTypeDefn(toAst(spaceTypeDefnContext));
        }

        log.info("Parse stats #parse nodes: " + countParseNodes);
        return parseUnitAST;
    }

    private ImportExpr toAst(SpaceParser.ImportStatementContext importStatementCtxt) {
        SpaceParser.AliasedSpacePathExprContext aliasedSpacePathExprContext =
            importStatementCtxt.aliasedSpacePathExpr();
        SpaceParser.AliasContext alias = aliasedSpacePathExprContext.alias();
        return astFactory.newImportExpr(toSI(importStatementCtxt),
                                        toTypeRef(aliasedSpacePathExprContext.metaRefExpr()),
                                        alias != null ? toText(alias.identifier()) : null);
    }

    private PackageDecl toAst(SpaceParser.PackageStatementContext packageStatementContext) {
        PackageDecl packageDecl = astFactory.newPackageDecl(toSI(packageStatementContext),
                                                            toAst(packageStatementContext.metaRefExpr(),
                                                                  MetaType.PACKAGE));
        return packageDecl;
    }

    private ExpressionChain toAst(SpaceParser.MetaRefExprContext metaRefExprContext, MetaType metaType) {
        ExpressionChain metaRefAST = astFactory.newMetaRefChain(toSI(metaRefExprContext), metaType, null);
        addMetaRefPartsRec(metaRefExprContext, metaRefAST);
        return metaRefAST;
    }

    private void addMetaRefPartsRec(SpaceParser.MetaRefExprContext metaRefExprContext, ExpressionChain metaRefAST)
    {
        if (metaRefExprContext == null)
            return;

        // do the add of new part
        for (SpaceParser.IdRefContext idRefContext : metaRefExprContext.idRef()) {
            metaRefAST.addNextPart(toNameRefExpr(idRefContext));
        }
        return;
    }

    private SimpleNameRefExpr toNameRefExpr(SpaceParser.IdRefContext idRefContext)
    {
        return toNameRefExpr(toNamePartExpr(idRefContext));
    }

    /** Special override needs to take Identifier, not IdRef */
    private SimpleNameRefExpr toNameRefExpr(SpaceParser.IdentifierContext identifierContext)
    {
        return toNameRefExpr(toNamePartExpr(identifierContext));
    }

    public TypeDefnImpl toAst(SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext) {
        logTrans(spaceTypeDefnContext);
        TypeDefnImpl complexTypeImpl = null;
        complexTypeImpl = astFactory.newTypeDefn(
            toSI(spaceTypeDefnContext),
            astFactory.newNamePart(toSI(spaceTypeDefnContext.identifier()), toText(spaceTypeDefnContext.identifier()))
        );
//        spaceTypeDefnContext.accessModifier();
//        spaceTypeDefnContext.defnTypeModifier();
//        spaceTypeDefnContext.elementDefnHeader();
        //

        if (spaceTypeDefnContext.ExtendsKeyword() != null) {
            //            spaceTypeDefnContext.spacePathList();
        }

        setProjection(complexTypeImpl, spaceTypeDefnContext.spaceTypeDefnBody());

        return complexTypeImpl;
    }

    private void setProjection(TypeDefn typeDefn, SpaceParser.SpaceTypeDefnBodyContext spaceTypeDefnBodyContext) {
        logTrans(spaceTypeDefnBodyContext);
        SourceInfo bodySourceInfo = toSI(spaceTypeDefnBodyContext);
//        SpaceTypeDefnBody typeDefnBodyAST =
//            astFactory.newTypeDefnBody(bodySourceInfo);

//        StatementBlock initBlock = astFactory.newStatementBlock(bodySourceInfo);
//        typeDefnBodyAST.setInitBlock(initBlock);

        List<SpaceParser.VariableDefnStmtContext> varDefCtxts = spaceTypeDefnBodyContext.variableDefnStmt();
        if (varDefCtxts != null && !varDefCtxts.isEmpty()) {
            for (SpaceParser.VariableDefnStmtContext variableDefnStmtContext : varDefCtxts) {
                // Add var def
                VariableDeclImpl variableDeclAST = toAst(variableDefnStmtContext.variableDefn());
                typeDefn.addVariableDecl(variableDeclAST);
                // Add assignment if there is one
                extractInit(typeDefn, variableDeclAST, variableDefnStmtContext, ScopeKind.OBJECT);
            }
        }

        List<SpaceParser.AssociationDefnStmtContext> assocDefCtxts = spaceTypeDefnBodyContext.associationDefnStmt();
        if (assocDefCtxts != null && !assocDefCtxts.isEmpty()) {
            for (SpaceParser.AssociationDefnStmtContext assocDefCtx : assocDefCtxts) {
                // Add declarative element
                AssociationDefnImpl assocDecl = toAst(astFactory.newTypeRef(toSI(assocDefCtx), typeDefn),
                                                            assocDefCtx.associationDefn());
                typeDefn.addAssociationDecl(assocDecl);
                // Add assignment expr if it exists
                extractInit(typeDefn, assocDecl, assocDefCtx, ScopeKind.OBJECT);
            }
        }

        List<SpaceParser.FunctionDefnContext> actionDefCtxts = spaceTypeDefnBodyContext.functionDefn();
        if (actionDefCtxts != null && !actionDefCtxts.isEmpty()) {
            for (SpaceParser.FunctionDefnContext functionDefnCtx : actionDefCtxts) {
                typeDefn.addFunctionDefn(toAst(functionDefnCtx));
            }
        }

//        List<SpaceParser.SpaceTypeDefnContext> childSpaceTypeDefnCtxts = spaceTypeDefnBodyContext.spaceTypeDefn();
        // TODO Handle sub-space defn

        return;
    }

    private void extractInit(ContextDatumDefn datumDefnContext,
                             AssociationDefn lhsDatumDecl,
                             SpaceParser.AssociationDefnStmtContext assocDefCtx,
                             ScopeKind scopeKind)
    {
        SpaceParser.IdentifierContext identifierCtxt =
            assocDefCtx.associationDefn().associationDecl().identifier();
        SpaceParser.RightAssignmentExprContext rightAssignmentExprContext =
            assocDefCtx.associationDefn().rightAssignmentExpr();
        if (rightAssignmentExprContext != null) {
            ExpressionChain lhsExpressionChain = astFactory.newDatumRef(toSI(identifierCtxt), lhsDatumDecl, scopeKind);
            datumDefnContext.addInitExpression(
                new ExprStatement<>(toAst(lhsExpressionChain, rightAssignmentExprContext))
            );
        }
    }

    private void extractInit(ContextDatumDefn datumDefnContext,
                             Declaration lhsDatumDecl,
                             SpaceParser.VariableDefnStmtContext variableDefnStmtContext,
                             ScopeKind scopeKind)
    {
        SpaceParser.IdentifierContext identifierCtxt =
            variableDefnStmtContext.variableDefn().variableDecl().identifier();
        SpaceParser.RightAssignmentExprContext rightAssignmentExprContext =
            variableDefnStmtContext.variableDefn().rightAssignmentExpr();
        if (rightAssignmentExprContext != null) {
            ExpressionChain lhsExpressionChain = astFactory.newDatumRef(toSI(identifierCtxt), lhsDatumDecl, scopeKind);
//            AstUtils.addNewMetaRefParts(lhsExpressionChain, toSI(identifierCtxt), toText(identifierCtxt));
            datumDefnContext.addInitExpression(
                new ExprStatement<>(toAst(lhsExpressionChain, rightAssignmentExprContext))
            );
        }
    }

    private AssignmentExpr toAst(ExpressionChain lhsExpressionChain,
                                 SpaceParser.RightAssignmentExprContext rightAssignmentExprContext)
    {
        logTrans(rightAssignmentExprContext);
        AssignmentExpr assignmentExprAST =
            astFactory.newAssignmentExpr(toSI(rightAssignmentExprContext));
        //
        assignmentExprAST.setLeftSideDatumRef(lhsExpressionChain);
        //
        assignmentExprAST.setRightSideValueExpr(toAst(rightAssignmentExprContext.valueExprChain()));
        return assignmentExprAST;
    }

    private AssociationDefnImpl toAst(TypeRef fromTypeRef, SpaceParser.AssociationDefnContext assocDefCtx) {
        logTrans(assocDefCtx);
        SpaceParser.ComplexOptCollTypeRefContext complexTypeRefContext = assocDefCtx.associationDecl().complexOptCollTypeRef();
        return astFactory.newAssociationDecl(
            toSI(assocDefCtx),
            toText(assocDefCtx.associationDecl().identifier()),
            fromTypeRef,
            toAst(complexTypeRefContext)
        );
    }

    private AssociationDefnImpl toAst(ContextDatumDefn contextDatumDefn, SpaceParser.AssociationDefnContext assocDefCtx) {
        logTrans(assocDefCtx);
        SpaceParser.ComplexOptCollTypeRefContext complexTypeRefContext = assocDefCtx.associationDecl().complexOptCollTypeRef();
        return astFactory.newAssociationDecl(
            toSI(assocDefCtx),
            toText(assocDefCtx.associationDecl().identifier()),
            contextDatumDefn,
            toAst(complexTypeRefContext)
        );
    }

    private TypeRefImpl toAst(SpaceParser.ComplexOptCollTypeRefContext complexTypeRefContext) {
        logTrans(complexTypeRefContext);
        TypeRefImpl typeRefAst = null;
        if (complexTypeRefContext.complexTypeRef().metaRefExpr() != null) {
            typeRefAst = toTypeRef(complexTypeRefContext.complexTypeRef().metaRefExpr());
        }
        if (complexTypeRefContext.anyCollectionMarker()!= null) {
            List<SpaceParser.AnyCollectionMarkerContext> anyCollectionMarkerContexts =
                complexTypeRefContext.anyCollectionMarker();
            List<TypeRefImpl.CollectionType> astCollTypes = new LinkedList<>();
            for (SpaceParser.AnyCollectionMarkerContext markerContext : anyCollectionMarkerContexts) {
                astCollTypes.add(toAst(markerContext));
            }
        }
        return typeRefAst;
    }

    private NamePartExpr toNamePartExpr(SpaceParser.PrimitiveTypeNameContext primitiveTypeNameContext) {
        logTrans(primitiveTypeNameContext);
        return astFactory.newNamePartExpr(toSI(primitiveTypeNameContext), PathOperEnum.ASSOC_NAV,
                                          primitiveTypeNameContext.getText());
    }

    private TypeRefImpl.CollectionType toAst(SpaceParser.AnyCollectionMarkerContext collDelimsContext) {
        logTrans(collDelimsContext);
        return collDelimsContext.sequenceMarker() != null ? TypeRefImpl.CollectionType.SEQUENCE
            : collDelimsContext.setMarker() != null ? TypeRefImpl.CollectionType.SET
            : null;
    }

    private TypeRefImpl toTypeRef(SpaceParser.MetaRefExprContext spacePathExprContext) {
        logTrans(spacePathExprContext);
        SimpleNameRefExpr nsRefPart =
            spacePathExprContext.languageKey() != null ? toAst(spacePathExprContext.languageKey()) : null;
        TypeRefImpl typeRef =
            astFactory.newTypeRef(toSI(spacePathExprContext), null, nsRefPart);
        addMetaRefPartsRec(spacePathExprContext, typeRef);
        return typeRef;
    }

    private SimpleNameRefExpr toAst(SpaceParser.LanguageKeyContext languageKeyContext) {
        return astFactory.newNameRefExpr(toSI(languageKeyContext), toText(languageKeyContext.idRef()));
    }

    private ExpressionChain toMetaRefChain(SpaceParser.MetaRefExprContext spacePathExprContext, MetaType metaType) {
        logTrans(spacePathExprContext);
        if (spacePathExprContext == null)
            return null;
        SimpleNameRefExpr nsReference = spacePathExprContext.languageKey() != null ?
            toNameRefExpr(spacePathExprContext.languageKey().idRef()) : null;
        ExpressionChain expressionChain = astFactory.newMetaRefChain(toSI(spacePathExprContext), metaType, nsReference);
        addMetaRefPartsRec(spacePathExprContext, expressionChain);
        return expressionChain;
    }

    private SpaceFunctionDefn toAst(SpaceParser.FunctionDefnContext functionDefnContext) {
        logTrans(functionDefnContext);
        SourceInfo sourceInfo = toSI(functionDefnContext);
        SpaceFunctionDefn functionDefnAST = astFactory
            .newSpaceFunctionDefn(sourceInfo, toText(functionDefnContext.identifier()),
                                  toAst(functionDefnContext.anyTypeRef()));
        functionDefnAST.setArgSpaceTypeDefn(toAst(functionDefnContext.indParams));
        //
        SpaceParser.StatementBlockContext statementBlockContext = functionDefnContext.statementBlock();
        functionDefnAST.setStatementBlock(toAst(statementBlockContext));
        //
        List<SpaceParser.DatumDefnStmtContext> datumDefnStmtContexts = statementBlockContext.datumDefnStmt();
        for (SpaceParser.DatumDefnStmtContext datumDefnStmtContext : datumDefnStmtContexts) {
            if (datumDefnStmtContext.variableDefnStmt() != null) {
                functionDefnAST.getStatementBlock().addVariableDecl(
                    toAst(datumDefnStmtContext.variableDefnStmt().variableDefn()));
            }
            else if (datumDefnStmtContext.associationDefnStmt() != null) {
                functionDefnAST.getStatementBlock().addAssociationDecl(
                    toAst(
                        (ContextDatumDefn) functionDefnAST.getStatementBlock(),
                        datumDefnStmtContext.associationDefnStmt().associationDefn()
                    )
                );
            }
        }

        return functionDefnAST;
    }

    private TypeRefImpl toAst(SpaceParser.AnyTypeRefContext anyTypeRefContext) {
        logTrans(anyTypeRefContext);
        TypeRefImpl typeRefAst = null;
        if (anyTypeRefContext != null) {
            if (anyTypeRefContext.complexOptCollTypeRef() != null) {
                typeRefAst = toAst(anyTypeRefContext.complexOptCollTypeRef());
            }
            else if (anyTypeRefContext.primitiveOptSeqTypeRef() != null) {
                typeRefAst = toTypeRef(anyTypeRefContext.primitiveOptSeqTypeRef());
            }
        }
        return typeRefAst;
    }

    private TypeRef toAst(SpaceParser.VoidTypeNameContext voidTypeNameContext) {
        logTrans(voidTypeNameContext);
        SourceInfo sourceInfo = toSI(voidTypeNameContext);
        TypeRefImpl typeRef = astFactory.newTypeRef(sourceInfo, null, null);
        typeRef.addNextPart(
            astFactory.newNameRefExpr(
                astFactory.newNamePartExpr(sourceInfo, PathOperEnum.ASSOC_NAV, voidTypeNameContext.getText())
            )
        );

        return typeRef;
    }

    private TypeRefImpl toTypeRef(SpaceParser.PrimitiveOptSeqTypeRefContext primitiveOptSeqTypeRefContext) {
        logTrans(primitiveOptSeqTypeRefContext);
        TypeRefImpl typeRefAst = null;
        SourceInfo sourceInfo = toSI(primitiveOptSeqTypeRefContext);
        if (primitiveOptSeqTypeRefContext.sequenceMarker() != null) {
            List<SpaceParser.SequenceMarkerContext> markerContexts = primitiveOptSeqTypeRefContext.sequenceMarker();
            List<TypeRefImpl.CollectionType> astCollTypes = new LinkedList<>();
            for (SpaceParser.SequenceMarkerContext markerContext : markerContexts) {
                astCollTypes.add(TypeRefImpl.CollectionType.SEQUENCE);
            }
            typeRefAst = astFactory.newTypeRef(sourceInfo, astCollTypes, null);
        }
        else {
            typeRefAst = astFactory.newTypeRef(sourceInfo, null, null);
        }
        typeRefAst.addNextPart(
            toNameRefExpr(toNamePartExpr(primitiveOptSeqTypeRefContext.primitiveTypeName()))
        );
        return typeRefAst;
    }

    private TypeDefnImpl toAst(SpaceParser.ParameterDefnListContext parameterDeclCtxt) {
        logTrans(parameterDeclCtxt);
        SourceInfo sourceInfo = toSI(parameterDeclCtxt);
        TypeDefnImpl typeDefn =
            astFactory.newTypeDefn(sourceInfo, astFactory.newNamePart(sourceInfo, "func args"));
//        typeDefn.setBody(astFactory.newTypeDefnBody(sourceInfo));
        List<SpaceParser.ParameterDeclContext> parameterDeclContexts = parameterDeclCtxt.parameterDecl();
        for (SpaceParser.ParameterDeclContext parameterDeclContext : parameterDeclContexts) {
            if (parameterDeclContext.variableDecl() != null)
                typeDefn.addVariableDecl(toAst(parameterDeclContext.variableDecl()));
            else if (parameterDeclContext.associationDecl() != null) {
                typeDefn.addAssociationDecl(toAst(null, parameterDeclContext.associationDecl()));
            }
        }
        return typeDefn;
    }

    private AssociationDefnImpl toAst(TypeRef fromTypeRef, SpaceParser.AssociationDeclContext assocDeclCtxt) {
        logTrans(assocDeclCtxt);
        return astFactory.newAssociationDecl(
            toSI(assocDeclCtxt),
            toText(assocDeclCtxt.identifier()),
            fromTypeRef,
            toAst(assocDeclCtxt.complexOptCollTypeRef())
        );
    }

    private StatementBlock toAst(SpaceParser.StatementBlockContext statementBlockContext) {
        logTrans(statementBlockContext);
        StatementBlock statementBlockAST = astFactory.newStatementBlock(toSI(statementBlockContext));
        List<SpaceParser.DatumDefnStmtContext> datumDefnStmtContexts = statementBlockContext.datumDefnStmt();
        List<SpaceParser.StatementContext> statementContexts = statementBlockContext.statement();
        //
        for (SpaceParser.DatumDefnStmtContext datumDefnStmtContext : datumDefnStmtContexts) {
            if (datumDefnStmtContext.variableDefnStmt() != null) {
                VariableDeclImpl variableDeclAst = toAst(datumDefnStmtContext.variableDefnStmt().variableDefn());
                statementBlockAST.addVariableDecl(variableDeclAst);
                extractInit(statementBlockAST, variableDeclAst, datumDefnStmtContext.variableDefnStmt(), ScopeKind.BLOCK);
            }
            else {
                SpaceParser.AssociationDefnStmtContext associationDefnStmtContext =
                    datumDefnStmtContext.associationDefnStmt();
                if (associationDefnStmtContext != null) {
                    AssociationDefnImpl associationDecl =
                        toAst(statementBlockAST, associationDefnStmtContext.associationDefn());
                    statementBlockAST.addAssociationDecl(
                        associationDecl
                    );
                    extractInit(statementBlockAST, associationDecl, associationDefnStmtContext, ScopeKind.BLOCK);
                }
            }
        }
        //
        for (SpaceParser.StatementContext statementContext : statementContexts) {
            // choice: expression or a nested block
            SpaceParser.ExpressionContext expressionCtxt = statementContext.expression();
            SpaceParser.IfStatementContext ifStatementContext = statementContext.ifStatement();
            SpaceParser.ReturnStatementContext returnStatementContext = statementContext.returnStatement();
            SpaceParser.StatementBlockContext nestedStatementBlockContext = statementContext.statementBlock();
            if (expressionCtxt != null) {
                statementBlockAST.addValueExpr(toAst(expressionCtxt));
            }
            else if (ifStatementContext != null) {
                // TODO
            }
            else if (returnStatementContext != null) {
                statementBlockAST.addStatement(toAst(returnStatementContext));
            }
            else if (nestedStatementBlockContext != null) {
                statementBlockAST.addStatement(toAst(nestedStatementBlockContext));
            }
        }
        return statementBlockAST;
    }

    private ReturnExpr toAst(SpaceParser.ReturnStatementContext returnStatementContext) {
        logTrans(returnStatementContext);
        ReturnExpr expr = astFactory.newReturnExpr(toSI(returnStatementContext),
                                                   toAst(returnStatementContext.valueExprChain()));
        return expr;
    }

    private ValueExpr toAst(SpaceParser.ExpressionContext expressionCtxt) {
        logTrans(expressionCtxt);
        ValueExpr valueExprAST = null;
        if (expressionCtxt.variableDefn() != null) {
            // creates just the assignment expr
            valueExprAST = toAst(toMetaRefChain(expressionCtxt.variableDefn().variableDecl().identifier()),
                                 expressionCtxt.variableDefn().rightAssignmentExpr());
        }
        else if (expressionCtxt.associationDefn() != null) {
            // creates just the assignment expr
            valueExprAST =
                toAst(toMetaRefChain(expressionCtxt.associationDefn().associationDecl().identifier()),
                      expressionCtxt.associationDefn().rightAssignmentExpr());
        }
        else if (expressionCtxt.functionCallExpr() != null) {
            valueExprAST = toAst(expressionCtxt.functionCallExpr());
        }
        else if (expressionCtxt.assignmentExpr() != null) {
            valueExprAST = toAst(expressionCtxt.assignmentExpr());
        }
        else if (expressionCtxt.valueExprChain() != null) {
            valueExprAST = toAst(expressionCtxt.valueExprChain());
        }
        else {
            throw new SpaceX("don't know how to transform " + expressionCtxt);
        }
        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.SymbolicExprContext symbolicExprContext) {
        logTrans(symbolicExprContext);
        ValueExpr valueExpr = null;
        // choice: 1 of
        SpaceParser.BinaryOperExprContext binaryOperExprContext = symbolicExprContext.binaryOperExpr();
//        SpaceParser.UnaryOperExprContext unaryOperExprContext = operatorExprContext.unaryOperExpr();
//        SpaceParser.OperatorExprContext nestedOperatorExprContext = operatorExprContext.operatorExpr();
        //
//        if (nestedOperatorExprContext != null) {
//            valueExpr = toAst(nestedOperatorExprContext);
//        }
        if (binaryOperExprContext != null) {
            valueExpr = toAst(binaryOperExprContext);
        }
//        else if (unaryOperExprContext != null) {
//            valueExpr = toAst(unaryOperExprContext);
//        }
        return valueExpr;
    }

    private ValueExpr toAst(SpaceParser.UnaryOperExprContext unaryOperExprContext) {
        logTrans(unaryOperExprContext);
        return astFactory.newOperatorExpr(toSI(unaryOperExprContext),
                                          toAst(unaryOperExprContext.unaryOper()),
                                          toAst(unaryOperExprContext.valueExprChain()));
    }

    private Operators.Operator toAst(SpaceParser.UnaryOperContext unaryOperContext) {
        return toAstOper(unaryOperContext.BooleanUnaryOper());
    }

    private ValueExpr toAst(SpaceParser.BinaryOperExprContext binaryOperExprContext) {
        logTrans(binaryOperExprContext);
        return astFactory.newOperatorExpr(toSI(binaryOperExprContext),
                                          toAst(binaryOperExprContext.binaryOper()),
                                          toAst(binaryOperExprContext.valueExprChain(0)),
                                          toAst(binaryOperExprContext.valueExprChain(1))
        );
    }

    private Operators.Operator toAst(SpaceParser.BinaryOperContext binaryOperContext) {
        // choice
        TerminalNode numTermNode = binaryOperContext.NumericBinaryOper();
        TerminalNode boolTermNode = binaryOperContext.BooleanBinaryOper();
        TerminalNode compTermNode = binaryOperContext.ComparisonOper();
        //
        TerminalNode operatorTermNode = null;
        if (numTermNode != null)
            operatorTermNode = numTermNode;
        else if (boolTermNode != null)
            operatorTermNode = boolTermNode;
        else if (compTermNode != null)
            operatorTermNode = compTermNode;
        //
        return toAstOper(operatorTermNode);
    }

    private Operators.Operator toAstOper(TerminalNode terminalNode) {
        logTrans(terminalNode);
        Operators.Operator operatorAST = operSymbolMap.get(terminalNode.getSymbol().getText());
        return operatorAST;
    }

    private void logTrans(TerminalNode terminalNode) {

    }

    private ExpressionChain toMetaRefChain(SpaceParser.IdRefContext idRefContext) {
        logTrans(idRefContext);
        ExpressionChain expressionChain = astFactory.newMetaRefChain(toSI(idRefContext), MetaType.DATUM, null);
        expressionChain.addNextPart(toNameRefExpr(idRefContext));
        return expressionChain;
    }

    private ExpressionChain toMetaRefChain(SpaceParser.IdentifierContext identifierContext) {
        logTrans(identifierContext);
        ExpressionChain expressionChain = astFactory.newMetaRefChain(toSI(identifierContext), MetaType.DATUM, null);
        expressionChain.addNextPart(toNameRefExpr(identifierContext));
        return expressionChain;
    }

    private SimpleNameRefExpr toNameRefExpr(NamePartExpr namePartExpr) {
        return astFactory.newNameRefExpr(namePartExpr);
    }

    private NamePartExpr toNamePartExpr(SpaceParser.IdentifierContext identifierCtxt) {
        logTrans(identifierCtxt);
        return astFactory.newNamePartExpr(toSI(identifierCtxt),
                                          null,
                                          toText(identifierCtxt));
    }

    private NamePartExpr toNamePartExpr(SpaceParser.IdRefContext idRefContext) {
        logTrans(idRefContext);
        return astFactory.newNamePartExpr(toSI(idRefContext),
                                          null,
                                          toText(idRefContext));
    }

    private AssignmentExpr toAst(SpaceParser.AssignmentExprContext assignmentExprContext) {
        logTrans(assignmentExprContext);
        return toAst(
            toMetaRefChain(assignmentExprContext.metaRefExpr(), MetaType.DATUM),
            assignmentExprContext.rightAssignmentExpr()
        );
    }

    private FunctionCallExpr toAst(SpaceParser.FunctionCallExprContext functionCallExprContext) {
        logTrans(functionCallExprContext);
        FunctionCallExpr functionCallExpr =
            astFactory.newFunctionCallExpr(toSI(functionCallExprContext));
        //
        toMetaRefChain(functionCallExprContext.idRef());
        functionCallExpr.setFunctionRef(
            astFactory.newNameRefExpr(toNamePartExpr(functionCallExprContext.idRef()))
        );
        //
        if (functionCallExprContext.argList().valueExprChain().size() > 0) {
            // more common
            functionCallExpr.setArgValueExpr(toAst(functionCallExprContext.argList().valueExprChain().get(0)));
        }
        return functionCallExpr;
    }

    private TupleValueList toAst(SpaceParser.TupleValueListContext tupleValueListContext) {
        logTrans(tupleValueListContext);
        TupleValueList tupleValueList = astFactory.newTupleExpr(toSI(tupleValueListContext));
        List<ValueExpr> tupleArgs = new LinkedList<>();
        SpaceParser.ValueOrAssignmentExprListContext callArgExprCtxts =
            tupleValueListContext.valueOrAssignmentExprList();
        for (SpaceParser.ValueOrAssignmentExprContext callArgContext : callArgExprCtxts.valueOrAssignmentExpr()) {
            tupleArgs.add(toAst(callArgContext));
        }
        tupleValueList.setValueExprs(tupleArgs);
        return tupleValueList;
    }

    /** Then general translator from ANTLR expression to AST expression.
     *  A bit complex since ANTRL trees provide no polymorphism, per se.
     */
    private ValueExpr toAst(SpaceParser.ValueOrAssignmentExprContext valueOrAssignExprCtxt) {
        logTrans(valueOrAssignExprCtxt);
        ValueExpr rightSide = null;
        SpaceParser.ValueExprChainContext valueExprChainContext = valueOrAssignExprCtxt.valueExprChain();
        SpaceParser.AssignmentExprContext assignmentExprContext = valueOrAssignExprCtxt.assignmentExpr();
        if (valueExprChainContext != null)
            rightSide = toAst(valueExprChainContext);
        else {
            rightSide = toAst(assignmentExprContext);
//            throw new IllegalArgumentException("not yet handling assignment-based value exprs");
        }
        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ValueExprChainContext valueExprChainContext) {
        logTrans(valueExprChainContext);
        ValueExpr valueExprAST = null;
        if (valueExprChainContext != null) {
            ValueExpr atomicValueExpr = toAst(valueExprChainContext.atomicValueExpr());
            valueExprAST = atomicValueExpr;
            // 2 thru last
            if (valueExprChainContext.namedRefValueExpr().size() > 0) {
                ValueExprChain astChain = astFactory.newValueExprChain(toSI(valueExprChainContext));
                valueExprAST = astChain;
                astChain.addValueExpr(atomicValueExpr);
                for (SpaceParser.NamedRefValueExprContext namedRefValueExprContext :
                        valueExprChainContext.namedRefValueExpr())
                {
                    astChain.addValueExpr(toAst(namedRefValueExprContext));
                }
            }
        }
        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.AtomicValueExprContext atomicValueExprContext) {
        ValueExpr valueExprAST = null;
        SpaceParser.LiteralExprContext literalExprContext = atomicValueExprContext.literalExpr();
        SpaceParser.NamedRefValueExprContext namedRefValueExprContext = atomicValueExprContext.namedRefValueExpr();
        SpaceParser.TupleLiteralContext tupleLiteralContext = atomicValueExprContext.tupleLiteral();
        SpaceParser.SymbolicExprContext operatorExprContext = atomicValueExprContext.symbolicExpr();
        SpaceParser.SetLiteralContext setLiteralContext = atomicValueExprContext.setLiteral();
        if (literalExprContext != null) {
            valueExprAST = toAst(literalExprContext);
        }
        else if (tupleLiteralContext != null)
            valueExprAST = toAst(tupleLiteralContext);
        else if (namedRefValueExprContext != null)
            // nested
            valueExprAST = toAst(namedRefValueExprContext);
        else if (operatorExprContext != null) {
            valueExprAST = toAst(operatorExprContext);
        }
        else if (setLiteralContext != null) {
            valueExprAST = toAst(setLiteralContext);
        }
        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.LiteralExprContext literalExprContext) {
        ValueExpr valueExprAST = null;

        if (literalExprContext.scalarLiteral() != null)
            valueExprAST = toAst(literalExprContext.scalarLiteral());
        else if (literalExprContext.stringLiteral() != null)
            valueExprAST = toAst(literalExprContext.stringLiteral());
        else if (literalExprContext.tupleLiteral() != null)
            valueExprAST = toAst(literalExprContext.tupleLiteral());
        else if (literalExprContext.setLiteral() != null)
            valueExprAST = toAst(literalExprContext.setLiteral());
        else
            throw new IllegalArgumentException("");

        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.NamedRefValueExprContext namedRefValueExprContext) {
        ValueExpr valueExprAST = null;
        SpaceParser.MetaRefExprContext metaRefExprContext = namedRefValueExprContext.metaRefExpr();
        SpaceParser.FunctionCallExprContext functionCallExprContext = namedRefValueExprContext.functionCallExpr();
        if (metaRefExprContext != null) {
            valueExprAST = toMetaRefChain(metaRefExprContext, null);
        }
        else if (functionCallExprContext != null) {
            valueExprAST = toAst(functionCallExprContext);
        }
        return valueExprAST;
    }

    private NewSetExpr toAst(SpaceParser.SetLiteralContext setLiteralContext) {
        return astFactory.newNewSetExpr(toSI(setLiteralContext), toAst(setLiteralContext.anyTypeRef()));
    }

    private NewTupleExpr toAst(SpaceParser.TupleLiteralContext tupleLiteralContext) {
        logTrans(tupleLiteralContext);
        NewTupleExpr newTupleExpr = astFactory.newNewObjectExpr(toSI(tupleLiteralContext),
                                                                toAst(tupleLiteralContext.anyTypeRef()),
                                                                toAst(tupleLiteralContext.tupleValueList()));
        return newTupleExpr;
    }

    /**
     * Character string literals (CharactersSequences) produce, in effect, a 'new CharacterSequence("")'
     * call. */
    private SequenceLiteralExpr toAst(SpaceParser.StringLiteralContext stringLiteralContext) {
        logTrans(stringLiteralContext);
        SequenceLiteralExpr primitiveLiteralExpr = null;

        String stringWithDelims = stringLiteralContext.StringLiteral().getText();
        primitiveLiteralExpr = astFactory.newCharSeqLiteralExpr(
            toSI(stringLiteralContext),
            stringWithDelims.substring(1, stringWithDelims.length() - 1)
        );

        return primitiveLiteralExpr;
    }

    private PrimitiveLiteralExpr toAst(SpaceParser.ScalarLiteralContext scalarLiteralContext) {
        logTrans(scalarLiteralContext);
        PrimitiveLiteralExpr element = null;
        SpaceParser.BooleanLiteralContext boolLiteralCtxt = scalarLiteralContext.booleanLiteral();
        SpaceParser.IntegerLiteralContext intLiteralCtxt = scalarLiteralContext.integerLiteral();
        SpaceParser.FloatLiteralContext floatLiteralCtxt = scalarLiteralContext.floatLiteral();
        if (intLiteralCtxt != null)
            element = toAst(intLiteralCtxt);
        else if (boolLiteralCtxt != null)
            element = toAst(boolLiteralCtxt);
        else if (floatLiteralCtxt != null)
            element = toAst(floatLiteralCtxt);
        else
            throw new IllegalArgumentException(
                "don't yet know how to translate literal expression " + scalarLiteralContext);

        return element;
    }

    private PrimitiveLiteralExpr toAst(SpaceParser.FloatLiteralContext floatLiteralCtxt) {
        logTrans(floatLiteralCtxt);
        return astFactory
            .newPrimLiteralExpr(toSI(floatLiteralCtxt), NumPrimitiveTypeDefn.REAL,
                                floatLiteralCtxt.FloatLiteral().getSymbol().getText());
    }

    private PrimitiveLiteralExpr toAst(SpaceParser.BooleanLiteralContext boolLiteralCtxt) {
        logTrans(boolLiteralCtxt);
        return astFactory
            .newPrimLiteralExpr(toSI(boolLiteralCtxt), NumPrimitiveTypeDefn.BOOLEAN,
                                boolLiteralCtxt.BooleanLiteral().getSymbol().getText());
    }

    private PrimitiveLiteralExpr toAst(SpaceParser.IntegerLiteralContext intLiteralCtxt) {
        logTrans(intLiteralCtxt);
        return astFactory.newPrimLiteralExpr(toSI(intLiteralCtxt), NumPrimitiveTypeDefn.CARD,
                                             intLiteralCtxt.IntegerLiteral().getSymbol().getText());
    }

    private VariableDeclImpl toAst(SpaceParser.VariableDefnContext variableDefnContext) {
        logTrans(variableDefnContext);
        SpaceParser.VariableDeclContext varDeclCtxt = variableDefnContext.variableDecl();
        VariableDeclImpl element = toAst(varDeclCtxt);
        return element;
    }

    private VariableDeclImpl toAst(SpaceParser.VariableDeclContext varDeclCtxt)
    {
        logTrans(varDeclCtxt);
        return astFactory.newVariableDecl(
            toSI(varDeclCtxt),
            toText(varDeclCtxt.identifier()),
            toAst(varDeclCtxt.primitiveOptSeqTypeRef())
        );
    }

    private TypeRef toAst(SpaceParser.PrimitiveOptSeqTypeRefContext primitiveOptSeqTypeRefContext) {
        logTrans(primitiveOptSeqTypeRefContext);
        NumPrimitiveTypeDefn numPrimitiveTypeDefn =
            (NumPrimitiveTypeDefn) NumPrimitiveTypeDefn.valueOf(primitiveOptSeqTypeRefContext.primitiveTypeName().getText());
        if (primitiveOptSeqTypeRefContext.sequenceMarker() != null) {
            numPrimitiveTypeDefn.setArrayDepth(primitiveOptSeqTypeRefContext.sequenceMarker().size());
        }

        return astFactory.newTypeRef(toSI(primitiveOptSeqTypeRefContext), numPrimitiveTypeDefn);
    }

    private void logTrans(ParserRuleContext antlrRuleCtxt) {
        countParseNodes++;
        if (log.isTraceEnabled() && antlrRuleCtxt != null)
            log.trace("transforming " + antlrRuleCtxt.getClass().getSimpleName() + " at " +
                          toSI(antlrRuleCtxt));
    }

    private String toText(SpaceParser.IdentifierContext identifierContext) {
        return identifierContext.Identifier().getSymbol().getText();
    }

    private String toText(SpaceParser.IdRefContext idRefContext) {
        return idRefContext.Identifier().getSymbol().getText();
    }

    private SourceInfo toSI(ParserRuleContext parserRuleCtx) {
        return Antrl2AstMapping.toAst(srcFile, parserRuleCtx);
    }

    private interface Transformer {
        ModelElement trans(ParseTree parseNode);
    }

}
