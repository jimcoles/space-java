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
import org.apache.log4j.Logger;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.AstUtils;
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

    private static final Logger log = Logger.getLogger(Antlr2AstTransform.class);
    // -------------------------------------------------------------------------
    //
    private Map<Class<? extends ParseTree>, Transformer> transformerMap = new HashMap<>();
    private ExecState state = ExecState.INITIALIZED;
    private List<AstLoadError> errors = new LinkedList();
    private AstFactory astFactory;
    private File srcFile;
    private Map<String, OperEnum> operSymbolMap = new TreeMap();
    private int countParseNodes = 0;

//    private ObjectFactory objBuilder = ObjectFactory.getInstance();

    {
        operSymbolMap.put("+", OperEnum.ADD);
        operSymbolMap.put("-", OperEnum.SUB);
        operSymbolMap.put("*", OperEnum.MULT);
        operSymbolMap.put("/", OperEnum.DIV);

        operSymbolMap.put("&", OperEnum.AND);
        operSymbolMap.put("&&", OperEnum.COND_AND);
        operSymbolMap.put("|", OperEnum.OR);
        operSymbolMap.put("||", OperEnum.COND_OR);
        operSymbolMap.put("!", OperEnum.NEGATION);

        operSymbolMap.put("==", OperEnum.EQ);
        operSymbolMap.put("<", OperEnum.LT);
        operSymbolMap.put(">", OperEnum.GT);
    }

    public Antlr2AstTransform(AstFactory astFactory, File srcFile) {
        this.astFactory = astFactory;
        this.srcFile = srcFile;
    }

    public ParseUnit transform(SpaceParser.ParseUnitContext spaceParseUnit) {
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
        ExpressionChain metaRefAST = astFactory.newMetaReference(toSI(metaRefExprContext), metaType, null);
        addMetaRefPartsRec(metaRefExprContext, metaRefAST);
        return metaRefAST;
    }

    private void addMetaRefPartsRec(SpaceParser.MetaRefExprContext metaRefExprContext, ExpressionChain metaRefAST)
    {
        if (metaRefExprContext == null)
            return;

        // do the add of new part
        for (SpaceParser.IdentifierContext identifierContext : metaRefExprContext.identifier()) {
            metaRefAST.addNextPart(toSingleMetaRefPart(identifierContext));
        }
        return;
    }

    private ExprLink toSingleMetaRefPart(SpaceParser.IdentifierContext identifierContext)
    {
        return astFactory.newMetaRefPart(toNamePartExpr(identifierContext));
    }

    public SpaceTypeDefn toAst(SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext) {
        logTrans(spaceTypeDefnContext);
        SpaceTypeDefn spaceTypeDefn = null;
        // TODO: 2/8/17 Not all spaces are Entities?
        spaceTypeDefn = astFactory.newSpaceTypeDefn(
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

        SpaceParser.SpaceTypeDefnBodyContext spaceTypeDefnBodyContext = spaceTypeDefnContext.spaceTypeDefnBody();

        spaceTypeDefn.setBody(toAst(spaceTypeDefnContext.spaceTypeDefnBody()));

        return spaceTypeDefn;
    }

    private SpaceTypeDefnBody toAst(SpaceParser.SpaceTypeDefnBodyContext spaceTypeDefnBodyContext) {
        logTrans(spaceTypeDefnBodyContext);
        SourceInfo bodySourceInfo = toSI(spaceTypeDefnBodyContext);
        SpaceTypeDefnBody typeDefnBodyAST =
            astFactory.newTypeDefnBody(bodySourceInfo);

//        StatementBlock initBlock = astFactory.newStatementBlock(bodySourceInfo);
//        typeDefnBodyAST.setInitBlock(initBlock);

        List<SpaceParser.VariableDefnStmtContext> varDefCtxts = spaceTypeDefnBodyContext.variableDefnStmt();
        if (varDefCtxts != null && !varDefCtxts.isEmpty()) {
            for (SpaceParser.VariableDefnStmtContext variableDefnStmtContext : varDefCtxts) {
                // Add var def
                VariableDeclImpl variableDeclAST = toAst(variableDefnStmtContext.variableDefn());
                typeDefnBodyAST.addVariableDecl(variableDeclAST);
                // Add assignment if there is one
                extractInit(typeDefnBodyAST, variableDefnStmtContext);
            }
        }

        List<SpaceParser.AssociationDefnStmtContext> assocDefCtxts = spaceTypeDefnBodyContext.associationDefnStmt();
        if (assocDefCtxts != null && !assocDefCtxts.isEmpty()) {
            for (SpaceParser.AssociationDefnStmtContext assocDefCtx : assocDefCtxts) {
                // Add declarative element
                typeDefnBodyAST.addAssociationDecl(toAst(assocDefCtx.associationDefn()));
                // Add assignment expr if it exists
                extractInit(typeDefnBodyAST, assocDefCtx);
            }
        }

        List<SpaceParser.FunctionDefnContext> actionDefCtxts = spaceTypeDefnBodyContext.functionDefn();
        if (actionDefCtxts != null && !actionDefCtxts.isEmpty()) {
            for (SpaceParser.FunctionDefnContext functionDefnCtx : actionDefCtxts) {
                typeDefnBodyAST.addFunctionDefn(toAst(functionDefnCtx));
            }
        }

//        List<SpaceParser.SpaceTypeDefnContext> childSpaceTypeDefnCtxts = spaceTypeDefnBodyContext.spaceTypeDefn();
        // TODO Handle sub-space defn

        return typeDefnBodyAST;
    }

    private void extractInit(StatementBlock blockAST, SpaceParser.AssociationDefnStmtContext assocDefCtx) {
        SpaceParser.RightAssignmentExprContext rightAssignmentExprContext =
            assocDefCtx.associationDefn().rightAssignmentExpr();
        if (rightAssignmentExprContext != null) {
            blockAST.addExpr(
                toAst(
                    toMetaRef(assocDefCtx.associationDefn().associationDecl().identifier()),
                    rightAssignmentExprContext
                )
            );
        }
    }

    private void extractInit(StatementBlock blockAST, SpaceParser.VariableDefnStmtContext variableDefnStmtContext)
    {
        SpaceParser.IdentifierContext identifierCtxt =
            variableDefnStmtContext.variableDefn().variableDecl().identifier();
        SpaceParser.RightAssignmentExprContext rightAssignmentExprContext =
            variableDefnStmtContext.variableDefn().rightAssignmentExpr();
        if (rightAssignmentExprContext != null) {
            ExpressionChain expressionChain = astFactory.newMetaReference(toSI(identifierCtxt), MetaType.DATUM, null);
            AstUtils.addNewMetaRefParts(expressionChain, toSI(identifierCtxt), toText(identifierCtxt));
            blockAST.addExpr(toAst(expressionChain, rightAssignmentExprContext));
        }
    }

    private AssignmentExpr toAst(ExpressionChain expressionChain,
                                 SpaceParser.RightAssignmentExprContext rightAssignmentExprContext)
    {
        logTrans(rightAssignmentExprContext);
        AssignmentExpr assignmentExprAST =
            astFactory.newAssignmentExpr(toSI(rightAssignmentExprContext));
        //
        assignmentExprAST.setMemberRef(expressionChain);
        //
        assignmentExprAST.setValueExpr(toAst(rightAssignmentExprContext.valueExpr()));
        return assignmentExprAST;
    }

    private AssociationDeclImpl toAst(SpaceParser.AssociationDefnContext assocDefCtx) {
        logTrans(assocDefCtx);
        SpaceParser.ComplexOptCollTypeRefContext complexTypeRefContext = assocDefCtx.associationDecl().complexOptCollTypeRef();
        return astFactory.newAssociationDecl(
            toSI(assocDefCtx),
            toText(assocDefCtx.associationDecl().identifier()),
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
        SimpleExprLink nsRefPart =
            spacePathExprContext.languageKey() != null ? toAst(spacePathExprContext.languageKey()) : null;
        TypeRefImpl typeRef =
            astFactory.newTypeRef(toSI(spacePathExprContext), null, nsRefPart);
        addMetaRefPartsRec(spacePathExprContext, typeRef);
        return typeRef;
    }

    private SimpleExprLink toAst(SpaceParser.LanguageKeyContext languageKeyContext) {
        return astFactory.newMetaRefPart(toSI(languageKeyContext), toText(languageKeyContext.identifier()));
    }

    private ExpressionChain toMetaRef(SpaceParser.MetaRefExprContext spacePathExprContext, MetaType metaType) {
        logTrans(spacePathExprContext);
        if (spacePathExprContext == null)
            return null;
        SimpleExprLink nsReference = spacePathExprContext.languageKey() != null ?
            astFactory.newMetaRefPart(toNamePartExpr(spacePathExprContext.languageKey().identifier()))
            : null;
        ExpressionChain expressionChain = astFactory.newMetaReference(toSI(spacePathExprContext), metaType, nsReference);
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
                    toAst(datumDefnStmtContext.associationDefnStmt().associationDefn()));
            }
        }

        return functionDefnAST;
    }

    private TypeRefImpl toAst(SpaceParser.AnyTypeRefContext anyTypeRefContext) {
        logTrans(anyTypeRefContext);
        TypeRefImpl typeRefAst = null;
        NamePartExpr pathExpr = null;
        if (anyTypeRefContext.complexOptCollTypeRef() != null) {
            typeRefAst = toAst(anyTypeRefContext.complexOptCollTypeRef());
        }
        else if (anyTypeRefContext.primitiveOptSeqTypeRef() != null) {
            typeRefAst = toTypeRef(anyTypeRefContext.primitiveOptSeqTypeRef());
        }
        return typeRefAst;
    }

    private TypeRef toAst(SpaceParser.VoidTypeNameContext voidTypeNameContext) {
        logTrans(voidTypeNameContext);
        SourceInfo sourceInfo = toSI(voidTypeNameContext);
        TypeRefImpl typeRef = astFactory.newTypeRef(sourceInfo, null, null);
        typeRef.addNextPart(
            astFactory.newMetaRefPart(
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
            astFactory.newMetaRefPart(toNamePartExpr(primitiveOptSeqTypeRefContext.primitiveTypeName()))
        );
        return typeRefAst;
    }

    private SpaceTypeDefn toAst(SpaceParser.ParameterDefnListContext parameterDeclCtxt) {
        logTrans(parameterDeclCtxt);
        SourceInfo sourceInfo = toSI(parameterDeclCtxt);
        SpaceTypeDefn typeDefn =
            astFactory.newSpaceTypeDefn(sourceInfo, astFactory.newNamePart(sourceInfo, "func args"));
        typeDefn.setBody(astFactory.newTypeDefnBody(sourceInfo));
        List<SpaceParser.ParameterDeclContext> parameterDeclContexts = parameterDeclCtxt.parameterDecl();
        for (SpaceParser.ParameterDeclContext parameterDeclContext : parameterDeclContexts) {
            if (parameterDeclContext.variableDecl() != null)
                typeDefn.addVariableDecl(toAst(parameterDeclContext.variableDecl()));
            else if (parameterDeclContext.associationDecl() != null) {
                typeDefn.addAssociationDecl(toAst(parameterDeclContext.associationDecl()));
            }
        }
        return typeDefn;
    }

    private AssociationDeclImpl toAst(SpaceParser.AssociationDeclContext assocDeclCtxt) {
        logTrans(assocDeclCtxt);
        return astFactory.newAssociationDecl(
            toSI(assocDeclCtxt),
            toText(assocDeclCtxt.identifier()),
            toAst(assocDeclCtxt.complexOptCollTypeRef())
        );
    }

    private StatementBlock toAst(SpaceParser.StatementBlockContext statementBlockContext) {
        logTrans(statementBlockContext);
        StatementBlock statementBlockAST =
            astFactory.newStatementBlock(toSI(statementBlockContext));
        List<SpaceParser.DatumDefnStmtContext> datumDefnStmtContexts = statementBlockContext.datumDefnStmt();
        List<SpaceParser.StatementContext> statementContexts = statementBlockContext.statement();
        //
        for (SpaceParser.DatumDefnStmtContext datumDefnStmtContext : datumDefnStmtContexts) {
            if (datumDefnStmtContext.variableDefnStmt() != null) {
                statementBlockAST.addVariableDecl(toAst(datumDefnStmtContext.variableDefnStmt().variableDefn()));
                extractInit(statementBlockAST, datumDefnStmtContext.variableDefnStmt());
            }
            else if (datumDefnStmtContext.associationDefnStmt() != null) {
                statementBlockAST
                    .addAssociationDecl(toAst(datumDefnStmtContext.associationDefnStmt().associationDefn()));
                extractInit(statementBlockAST, datumDefnStmtContext.associationDefnStmt());
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
                statementBlockAST.addExpr(toAst(expressionCtxt));
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
                                                   toAst(returnStatementContext.valueExpr()));
        return expr;
    }

    private ValueExpr toAst(SpaceParser.ExpressionContext expressionCtxt) {
        logTrans(expressionCtxt);
        ValueExpr valueExprAST = null;
        if (expressionCtxt.variableDefn() != null) {
            // creates just the assignment expr
            valueExprAST = toAst(toMetaRef(expressionCtxt.variableDefn().variableDecl().identifier()),
                                 expressionCtxt.variableDefn().rightAssignmentExpr());
        }
        else if (expressionCtxt.associationDefn() != null) {
            // creates just the assignment expr
            valueExprAST =
                toAst(toMetaRef(expressionCtxt.associationDefn().associationDecl().identifier()),
                      expressionCtxt.associationDefn().rightAssignmentExpr());
        }
        else if (expressionCtxt.functionCallExpr() != null) {
            valueExprAST = toAst(expressionCtxt.functionCallExpr());
        }
        else if (expressionCtxt.assignmentExpr() != null) {
            valueExprAST = toAst(expressionCtxt.assignmentExpr());
        }
        else if (expressionCtxt.valueExpr() != null) {
            valueExprAST = toAst(expressionCtxt.valueExpr());
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
                                          toAst(unaryOperExprContext.valueExpr()));
    }

    private OperEnum toAst(SpaceParser.UnaryOperContext unaryOperContext) {
        return toAstOper(unaryOperContext.BooleanUnaryOper());
    }

    private ValueExpr toAst(SpaceParser.BinaryOperExprContext binaryOperExprContext) {
        logTrans(binaryOperExprContext);
        return astFactory.newOperatorExpr(toSI(binaryOperExprContext),
                                          toAst(binaryOperExprContext.binaryOper()),
                                          toAst(binaryOperExprContext.valueExpr(0)),
                                          toAst(binaryOperExprContext.valueExpr(1))
        );
    }

    private OperEnum toAst(SpaceParser.BinaryOperContext binaryOperContext) {
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

    private OperEnum toAstOper(TerminalNode terminalNode) {
        logTrans(terminalNode);
        OperEnum operEnumAST = operSymbolMap.get(terminalNode.getSymbol().getText());
        return operEnumAST;
    }

    private void logTrans(TerminalNode terminalNode) {

    }

    private ExpressionChain toMetaRef(SpaceParser.IdentifierContext identifierCtxt) {
        logTrans(identifierCtxt);
        ExpressionChain expressionChain = astFactory.newMetaReference(toSI(identifierCtxt), MetaType.DATUM, null);
        expressionChain.addNextPart(astFactory.newMetaRefPart(toNamePartExpr(identifierCtxt)));
        return expressionChain;
    }

    private NamePartExpr toNamePartExpr(SpaceParser.IdentifierContext identifierCtxt) {
        logTrans(identifierCtxt);
        return astFactory.newNamePartExpr(toSI(identifierCtxt),
                                          null,
                                          toText(identifierCtxt));
    }

    private AssignmentExpr toAst(SpaceParser.AssignmentExprContext assignmentExprContext) {
        logTrans(assignmentExprContext);
        return toAst(
            toMetaRef(assignmentExprContext.metaRefExpr(), MetaType.DATUM),
            assignmentExprContext.rightAssignmentExpr()
        );
    }

    private FunctionCallExpr toAst(SpaceParser.FunctionCallExprContext functionCallExprContext) {
        logTrans(functionCallExprContext);
        FunctionCallExpr functionCallExpr =
            astFactory.newFunctionCallExpr(toSI(functionCallExprContext));
        //
        functionCallExpr.setFunctionRef(toMetaRef(functionCallExprContext.metaRefExpr(), MetaType.FUNCTION));
        //
        if (functionCallExprContext.valueExpr() != null) {
            // more common
            functionCallExpr.setArgValueExpr(toAst(functionCallExprContext.valueExpr()));
        }
        return functionCallExpr;
    }

    private TupleExpr toAst(SpaceParser.UntypedTupleLiteralContext untypedTupleLiteralCtxt) {
        logTrans(untypedTupleLiteralCtxt);
        TupleExpr tupleExpr = astFactory.newTupleExpr(toSI(untypedTupleLiteralCtxt));
        List<ValueExpr> tupleArgs = new LinkedList<>();
        SpaceParser.ValueOrAssignmentExprListContext callArgExprCtxts =
            untypedTupleLiteralCtxt.valueOrAssignmentExprList();
        for (SpaceParser.ValueOrAssignmentExprContext callArgContext : callArgExprCtxts.valueOrAssignmentExpr()) {
            tupleArgs.add(toAst(callArgContext));
        }
        tupleExpr.setValueExprs(tupleArgs);
        return tupleExpr;
    }

    /** Then general translator from ANTLR expression to AST expression.
     *  A bit complex since ANTRL trees provide no polymorphism, per se.
     */
    private ValueExpr toAst(SpaceParser.ValueOrAssignmentExprContext valueOrAssignExprCtxt) {
        logTrans(valueOrAssignExprCtxt);
        ValueExpr rightSide = null;
        SpaceParser.ValueExprContext valueExprContext = valueOrAssignExprCtxt.valueExpr();
        SpaceParser.AssignmentExprContext assignmentExprContext = valueOrAssignExprCtxt.assignmentExpr();
        if (valueExprContext != null)
            rightSide = toAst(valueExprContext);
        else {
            rightSide = toAst(assignmentExprContext);
//            throw new IllegalArgumentException("not yet handling assignment-based value exprs");
        }
        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ValueExprContext valueExprContext) {
        logTrans(valueExprContext);
        ValueExpr valueExprAST = null;
        if (valueExprContext != null) {
            valueExprAST = toAst(valueExprContext.atomicValueExpr());
            // 2 thru last
            if (valueExprContext.namedRefValueExpr().size() > 0) {
                ValueExprChain astChain = astFactory.newValueExprChain(toSI(valueExprContext));
                valueExprAST = astChain;
                astChain.addValueExpr(valueExprAST);
                for (SpaceParser.NamedRefValueExprContext namedRefValueExprContext :
                        valueExprContext.namedRefValueExpr())
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
        SpaceParser.NewObjectExprContext objectExprContext = atomicValueExprContext.newObjectExpr();
        SpaceParser.SymbolicExprContext operatorExprContext = atomicValueExprContext.symbolicExpr();
        SpaceParser.NewSetExprContext newSetExprContext = atomicValueExprContext.newSetExpr();
        if (literalExprContext != null) {
            valueExprAST = toAst(literalExprContext);
        }
        else if (objectExprContext != null)
            valueExprAST = toAst(objectExprContext);
        else if (namedRefValueExprContext != null)
            // nested
            valueExprAST = toAst(namedRefValueExprContext);
        else if (operatorExprContext != null) {
            valueExprAST = toAst(operatorExprContext);
        }
        else if (newSetExprContext != null) {
            valueExprAST = toAst(newSetExprContext);
        }
        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.LiteralExprContext literalExprContext) {
        ValueExpr valueExprAST = null;

        if (literalExprContext.scalarLiteral() != null)
            valueExprAST = toAst(literalExprContext.scalarLiteral());
        else if (literalExprContext.stringLiteral() != null)
            valueExprAST = toAst(literalExprContext.stringLiteral());
        else if (literalExprContext.untypedTupleLiteral() != null)
            valueExprAST = toAst(literalExprContext.untypedTupleLiteral());
        else
            throw new IllegalArgumentException("");

        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.NamedRefValueExprContext namedRefValueExprContext) {
        ValueExpr valueExprAST = null;
        SpaceParser.MetaRefExprContext metaRefExprContext = namedRefValueExprContext.metaRefExpr();
        SpaceParser.FunctionCallExprContext functionCallExprContext = namedRefValueExprContext.functionCallExpr();
        if (metaRefExprContext != null) {
            valueExprAST = toMetaRef(metaRefExprContext, MetaType.DATUM);
        }
        else if (functionCallExprContext != null) {
            valueExprAST = toAst(functionCallExprContext);
        }
        return valueExprAST;
    }

    private NewSetExpr toAst(SpaceParser.NewSetExprContext newSetExprContext) {
        return astFactory.newNewSetExpr(toSI(newSetExprContext), toAst(newSetExprContext.anyTypeRef()));
    }

    private NewTupleExpr toAst(SpaceParser.NewObjectExprContext newObjectExprContext) {
        logTrans(newObjectExprContext);
        NewTupleExpr newTupleExpr = astFactory.newNewObjectExpr(toSI(newObjectExprContext),
                                                                toAst(newObjectExprContext.anyTypeRef()),
                                                                toAst(newObjectExprContext.untypedTupleLiteral()));
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

    private SourceInfo toSI(ParserRuleContext parserRuleCtx) {
        return Antrl2AstMapping.toAst(srcFile, parserRuleCtx);
    }

    private interface Transformer {
        ModelElement trans(ParseTree parseNode);
    }

}
