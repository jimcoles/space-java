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

        for (SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext : spaceTypeDefnCtxts) {
            parseUnitAST.addChild(toAst(spaceTypeDefnContext));
        }

        log.info("Parse stats #parse nodes: " + countParseNodes);
        return parseUnitAST;
    }

    private PackageDecl toAst(SpaceParser.PackageStatementContext packageStatementContext) {
        PackageDecl packageDecl = astFactory.newPackageDecl(toSI(packageStatementContext),
                                                            toAst(packageStatementContext.spacePathExpr(),
                                                                  MetaType.PACKAGE));
        return packageDecl;
    }

    private MetaReference toAst(SpaceParser.SpacePathExprContext spacePathExprContext, MetaType metaType) {
        MetaReference metaReference = astFactory.newMetaReference(toSI(spacePathExprContext), metaType);
        metaReference.setFirstPart(toMetaRefPartList(spacePathExprContext, metaReference));
        return metaReference;
    }

    private MetaRefPart toMetaRefPartList(SpaceParser.SpacePathExprContext spacePathExprContext, MetaReference metaReference)
    {
        MetaRefPart metaRefPart = toMetaRefPart(spacePathExprContext, metaReference);
        SpaceParser.SpacePathExprContext nextPathExprPartCtxt = spacePathExprContext.spacePathExpr();
        while (nextPathExprPartCtxt != null) {
            MetaRefPart nextMetaRefPart = toMetaRefPart(spacePathExprContext, metaReference);
            metaRefPart.setNextRefPart(nextMetaRefPart);
            //
            nextPathExprPartCtxt = nextPathExprPartCtxt.spacePathExpr();
            metaRefPart = nextMetaRefPart;
        }
        return metaRefPart;
    }

    private MetaRefPart toMetaRefPart(SpaceParser.SpacePathExprContext spacePathExprContext,
                                      MetaReference metaReference)
    {
        return astFactory.newMetaRefPart(metaReference, toNamePartExpr(spacePathExprContext.identifier()));
    }

    public SpaceTypeDefn toAst(SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext) {
        logTrans(spaceTypeDefnContext);
        SpaceTypeDefn spaceTypeDefn = null;
        // TODO: 2/8/17 Not all spaces are Entities?
        spaceTypeDefn = astFactory.newSpaceTypeDefn(
            toSI(spaceTypeDefnContext),
            astFactory.newTextNode(toSI(spaceTypeDefnContext.identifier()), toText(spaceTypeDefnContext.identifier()))
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
                VariableDecl variableDeclAST = toAst(variableDefnStmtContext.variableDefn());
                typeDefnBodyAST.addVariableDecl(variableDeclAST);
                // Add assignment if there is one
                extractInit(typeDefnBodyAST, variableDefnStmtContext);
            }
        }

        List<SpaceParser.AssociationDefnStmtContext> assocDefCtxts = spaceTypeDefnBodyContext.associationDefnStmt();
        if (assocDefCtxts != null && !assocDefCtxts.isEmpty()) {
            for (SpaceParser.AssociationDefnStmtContext assocDefCtx : assocDefCtxts) {
                // Add declarative element
                typeDefnBodyAST.addAssocDecl(toAst(assocDefCtx.associationDefn()));
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
            MetaReference metaReference = astFactory.newMetaReference(toSI(identifierCtxt), MetaType.DATUM);
            metaReference.setFirstPart(
                astFactory.newMetaRefPart(metaReference, toSI(identifierCtxt), toText(identifierCtxt))
            );
            blockAST.addExpr(toAst(metaReference, rightAssignmentExprContext));
        }
    }

    private AssignmentExpr toAst(MetaReference metaReference,
                                 SpaceParser.RightAssignmentExprContext rightAssignmentExprContext)
    {
        logTrans(rightAssignmentExprContext);
        AssignmentExpr assignmentExprAST =
            astFactory.newAssignmentExpr(toSI(rightAssignmentExprContext));
        //
        assignmentExprAST.setMemberRef(metaReference);
        //
        assignmentExprAST.setValueExpr(toAst(rightAssignmentExprContext.valueExpr()));
        return assignmentExprAST;
    }

    private AssociationDecl toAst(SpaceParser.AssociationDefnContext assocDefCtx) {
        logTrans(assocDefCtx);
        SpaceParser.ComplexOptCollTypeRefContext complexTypeRefContext = assocDefCtx.associationDecl().complexOptCollTypeRef();
        return astFactory.newAssociationDecl(
            toSI(assocDefCtx),
            toText(assocDefCtx.associationDecl().identifier()),
            toAst(complexTypeRefContext)
        );
    }

    private TypeRef toAst(SpaceParser.ComplexOptCollTypeRefContext complexTypeRefContext) {
        logTrans(complexTypeRefContext);
        TypeRef typeRefAst = null;
        if (complexTypeRefContext.complexTypeRef().spacePathExpr() != null) {
            typeRefAst = toTypeRef(complexTypeRefContext.complexTypeRef().spacePathExpr());
        }
        if (complexTypeRefContext.anyCollectionMarker()!= null) {
            List<SpaceParser.AnyCollectionMarkerContext> anyCollectionMarkerContexts =
                complexTypeRefContext.anyCollectionMarker();
            List<TypeRef.CollectionType> astCollTypes = new LinkedList<>();
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

    private TypeRef.CollectionType toAst(SpaceParser.AnyCollectionMarkerContext collDelimsContext) {
        logTrans(collDelimsContext);
        return collDelimsContext.sequenceMarker() != null ? TypeRef.CollectionType.SEQUENCE
            : collDelimsContext.setMarker() != null ? TypeRef.CollectionType.SET
            : null;
    }

    private TypeRef toTypeRef(SpaceParser.SpacePathExprContext spacePathExprContext) {
        logTrans(spacePathExprContext);
        TypeRef typeRef = astFactory.newTypeRef(toSI(spacePathExprContext), null);
        typeRef.setFirstPart(toMetaRefPartList(spacePathExprContext, typeRef));
        return typeRef;
    }

    private MetaReference toMetaRef(SpaceParser.SpacePathExprContext spacePathExprContext, MetaType metaType) {
        logTrans(spacePathExprContext);
        if (spacePathExprContext == null)
            return null;
        MetaReference metaReference = astFactory.newMetaReference(toSI(spacePathExprContext), metaType);
        metaReference.setFirstPart(toMetaRefPartList(spacePathExprContext, metaReference));
        return metaReference;
    }

    private AbstractFunctionDefn toAst(SpaceParser.FunctionDefnContext functionDefnContext) {
        logTrans(functionDefnContext);
        SourceInfo sourceInfo = toSI(functionDefnContext);
        FunctionDefn functionDefnAST = astFactory
            .newSpaceFunctionDefn(sourceInfo, toText(functionDefnContext.identifier()),
                                  toAst(functionDefnContext.anyTypeRef()));
        functionDefnAST.setArgSpaceTypeDefn(toAst(functionDefnContext.parameterDefnList()));
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
                functionDefnAST.getStatementBlock().addAssocDecl(
                    toAst(datumDefnStmtContext.associationDefnStmt().associationDefn()));
            }
        }

        return functionDefnAST;
    }

    private TypeRef toAst(SpaceParser.AnyTypeRefContext anyTypeRefContext) {
        logTrans(anyTypeRefContext);
        TypeRef typeRefAst = null;
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
        TypeRef typeRef = astFactory.newTypeRef(sourceInfo, null);
        typeRef.setFirstPart(
            astFactory.newMetaRefPart(
                typeRef,
                astFactory.newNamePartExpr(sourceInfo, PathOperEnum.ASSOC_NAV, voidTypeNameContext.getText())
            )
        );

        return typeRef;
    }

    private TypeRef toTypeRef(SpaceParser.PrimitiveOptSeqTypeRefContext primitiveOptSeqTypeRefContext) {
        logTrans(primitiveOptSeqTypeRefContext);
        TypeRef typeRefAst = null;
        SourceInfo sourceInfo = toSI(primitiveOptSeqTypeRefContext);
        if (primitiveOptSeqTypeRefContext.sequenceMarker() != null) {
            List<SpaceParser.SequenceMarkerContext> markerContexts = primitiveOptSeqTypeRefContext.sequenceMarker();
            List<TypeRef.CollectionType> astCollTypes = new LinkedList<>();
            for (SpaceParser.SequenceMarkerContext markerContext : markerContexts) {
                astCollTypes.add(TypeRef.CollectionType.SEQUENCE);
            }
            typeRefAst = astFactory.newTypeRef(sourceInfo, astCollTypes);
        }
        else {
            typeRefAst = astFactory.newTypeRef(sourceInfo, null);
        }
        typeRefAst.setFirstPart(
            astFactory.newMetaRefPart(typeRefAst,
                                      toNamePartExpr(primitiveOptSeqTypeRefContext.primitiveTypeName())));
        return typeRefAst;
    }

    private SpaceTypeDefn toAst(SpaceParser.ParameterDefnListContext parameterDeclCtxt) {
        logTrans(parameterDeclCtxt);
        SourceInfo sourceInfo = toSI(parameterDeclCtxt);
        SpaceTypeDefn typeDefn =
            astFactory.newSpaceTypeDefn(sourceInfo, astFactory.newTextNode(sourceInfo, "func args"));
        typeDefn.setBody(astFactory.newTypeDefnBody(sourceInfo));
        List<SpaceParser.ParameterDeclContext> parameterDeclContexts = parameterDeclCtxt.parameterDecl();
        for (SpaceParser.ParameterDeclContext parameterDeclContext : parameterDeclContexts) {
            if (parameterDeclContext.variableDecl() != null)
                typeDefn.addVariable(toAst(parameterDeclContext.variableDecl()));
            else if (parameterDeclContext.associationDecl() != null) {
                typeDefn.addAssocDefn(toAst(parameterDeclContext.associationDecl()));
            }
        }
        return typeDefn;
    }

    private AssociationDecl toAst(SpaceParser.AssociationDeclContext assocDeclCtxt) {
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
                statementBlockAST.addAssocDecl(toAst(datumDefnStmtContext.associationDefnStmt().associationDefn()));
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
        else if (expressionCtxt.operatorExpr() != null) {
            valueExprAST = toAst(expressionCtxt.operatorExpr());
        }
//        else if (expressionCtxt.navCallChainExpr() != null) {
//
//        }
        else {
            throw new SpaceX("don't know how to transform " + expressionCtxt);
        }
        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.OperatorExprContext operatorExprContext) {
        logTrans(operatorExprContext);
        ValueExpr valueExpr = null;
        // choice: 1 of
        SpaceParser.BinaryOperExprContext binaryOperExprContext = operatorExprContext.binaryOperExpr();
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

    private MetaReference toMetaRef(SpaceParser.IdentifierContext identifierCtxt) {
        logTrans(identifierCtxt);
        MetaReference metaReference = astFactory.newMetaReference(toSI(identifierCtxt), MetaType.DATUM);
        metaReference.setFirstPart(astFactory.newMetaRefPart(metaReference, toNamePartExpr(identifierCtxt)));
        return metaReference;
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
            toMetaRef(assignmentExprContext.spacePathExpr(), MetaType.DATUM),
            assignmentExprContext.rightAssignmentExpr()
        );
    }

    private FunctionCallExpr toAst(SpaceParser.FunctionCallExprContext functionCallExprContext) {
        logTrans(functionCallExprContext);
        FunctionCallExpr functionCallExpr =
            astFactory.newFunctionCallExpr(toSI(functionCallExprContext));
        //
        functionCallExpr.setFunctionDefnRef(toMetaRef(functionCallExprContext.spacePathExpr(), MetaType.FUNCTION));
        //
        if (functionCallExprContext.argTupleOrRef() != null) {
            SpaceParser.UntypedTupleLiteralContext tupleLiteralCtxt =
                functionCallExprContext.argTupleOrRef().untypedTupleLiteral();
            SpaceParser.SpacePathExprContext spacePathExprCtxt =
                functionCallExprContext.argTupleOrRef().spacePathExpr();
            if (tupleLiteralCtxt != null) {
                // more common
                functionCallExpr.setArgTupleExpr(toAst(tupleLiteralCtxt));
            }
            else if (spacePathExprCtxt != null) {
                // argument as wrapped object
                MetaReference tupleRef = toMetaRef(spacePathExprCtxt, MetaType.DATUM);
                functionCallExpr.setArgTupleRef(tupleRef);
            }
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
            throw new IllegalArgumentException("not yet handling assignment argument exprs");
        }
        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ValueExprContext valueExprContext) {
        logTrans(valueExprContext);
        ValueExpr valueExprAST = null;
        if (valueExprContext != null) {
            // choices ...
            SpaceParser.LiteralExprContext literalExprContext = valueExprContext.literalExpr();
            SpaceParser.FunctionCallExprContext functionCallExprContext = valueExprContext.functionCallExpr();
            SpaceParser.NewObjectExprContext objectExprContext = valueExprContext.newObjectExpr();
            SpaceParser.SpacePathExprContext spacePathExprContext = valueExprContext.spacePathExpr();
            SpaceParser.OperatorExprContext operatorExprContext = valueExprContext.operatorExpr();
            SpaceParser.NewSetExprContext newSetExprContext = valueExprContext.newSetExpr();
            if (literalExprContext != null) {
                if (literalExprContext.scalarLiteral() != null)
                    valueExprAST = toAst(literalExprContext.scalarLiteral());
                else if (literalExprContext.stringLiteral() != null)
                    valueExprAST = toAst(literalExprContext.stringLiteral());
            }
            else if (objectExprContext != null)
                valueExprAST = toAst(objectExprContext);
            else if (functionCallExprContext != null)
                // nested
                valueExprAST = toAst(functionCallExprContext);
            else if (spacePathExprContext != null) {
                valueExprAST = toMetaRef(spacePathExprContext, MetaType.DATUM);
            }
            else if (operatorExprContext != null) {
                valueExprAST = toAst(operatorExprContext);
            }
            else if (newSetExprContext != null) {
                valueExprAST = toAst(newSetExprContext);
            }
        }
        return valueExprAST;
    }

    private NewSetExpr toAst(SpaceParser.NewSetExprContext newSetExprContext) {
        return astFactory.newNewSetExpr(toSI(newSetExprContext), toTypeRef(newSetExprContext.spacePathExpr()));
    }

    private NewObjectExpr toAst(SpaceParser.NewObjectExprContext newObjectExprContext) {
        logTrans(newObjectExprContext);
        NewObjectExpr newObjectExpr = astFactory.newNewObjectExpr(toSI(newObjectExprContext),
                                                                  toTypeRef(newObjectExprContext.spacePathExpr()),
                                                                  toAst(newObjectExprContext.untypedTupleLiteral()));
        return newObjectExpr;
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

    private VariableDecl toAst(SpaceParser.VariableDefnContext variableDefnContext) {
        logTrans(variableDefnContext);
        SpaceParser.VariableDeclContext varDeclCtxt = variableDefnContext.variableDecl();
        VariableDecl element = toAst(varDeclCtxt);
        return element;
    }

    private VariableDecl toAst(SpaceParser.VariableDeclContext varDeclCtxt)
    {
        logTrans(varDeclCtxt);
        return astFactory.newVariableDecl(
            toSI(varDeclCtxt),
            toText(varDeclCtxt.identifier()),
            toAst(varDeclCtxt.primitiveOptSeqTypeRef())
        );
    }

    private NumPrimitiveTypeDefn toAst(SpaceParser.PrimitiveOptSeqTypeRefContext primitiveOptSeqTypeRefContext) {
        logTrans(primitiveOptSeqTypeRefContext);
        NumPrimitiveTypeDefn numPrimitiveTypeDefn =
            (NumPrimitiveTypeDefn) NumPrimitiveTypeDefn.valueOf(primitiveOptSeqTypeRefContext.primitiveTypeName().getText());
        if (primitiveOptSeqTypeRefContext.sequenceMarker() != null) {
            numPrimitiveTypeDefn.setArrayDepth(primitiveOptSeqTypeRefContext.sequenceMarker().size());
        }

        return numPrimitiveTypeDefn;
    }

    private void logTrans(ParserRuleContext antlrRuleCtxt) {
        countParseNodes++;
        if (log.isDebugEnabled() && antlrRuleCtxt != null)
            log.debug("transforming " + antlrRuleCtxt.getClass().getSimpleName() + " at " +
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
