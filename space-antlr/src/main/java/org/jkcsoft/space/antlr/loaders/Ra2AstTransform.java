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
public class Ra2AstTransform {

    private static final Logger log = Logger.getLogger(Ra2AstTransform.class);
    // -------------------------------------------------------------------------
    //
    private Map<Class<? extends ParseTree>, Transformer> transformerMap = new HashMap<>();
    private ExecState state = ExecState.INITIALIZED;
    private List<AstLoadError> errors = new LinkedList();
    private AstFactory astFactory;
    private File srcFile;

    public Ra2AstTransform(AstFactory astFactory, File srcFile) {
        this.astFactory = astFactory;
        this.srcFile = srcFile;
    }

//    private ObjectFactory objBuilder = ObjectFactory.getInstance();

    public AstFactory transformAndBuild(ParseTree parseTreeRoot) {

//        visitNodeRecurse(parseTreeRoot);

        return astFactory;
    }

    private ModelElement lookupTrans(ParseTree node) {
        ModelElement trans = transformerMap.get(node.getClass()).trans(node);
        return trans;
    }

    public Schema transform(SpaceParser.ParseUnitContext spaceParseUnit) {
        log.info("transforming ANTLR parse tree to AST starting with root parse node.");
        logTrans(spaceParseUnit);
        Schema schema = astFactory.newAstSchema(Antrl2AstMapping.toAst(srcFile, spaceParseUnit), "user");
        List<SpaceParser.SpaceTypeDefnContext> spaceTypeDefnContexts = spaceParseUnit.spaceTypeDefn();
        for (SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext : spaceTypeDefnContexts) {
            schema.addSpaceDefn(toAst(spaceTypeDefnContext));
        }
        return schema;
    }

    public SpaceTypeDefn toAst(SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext) {
        logTrans(spaceTypeDefnContext);
        SpaceTypeDefn spaceTypeDefn = null;
        // TODO: 2/8/17 Not all spaces are Entities?
        spaceTypeDefn = astFactory.newSpaceTypeDefn(
            Antrl2AstMapping.toAst(srcFile, spaceTypeDefnContext),
            toText(spaceTypeDefnContext.identifier())
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
        SourceInfo bodySourceInfo = Antrl2AstMapping.toAst(srcFile, spaceTypeDefnBodyContext);
        SpaceTypeDefnBody typeDefnBodyAST =
            astFactory.newTypeDefnBody(bodySourceInfo);

//        StatementBlock initBlock = astFactory.newStatementBlock(bodySourceInfo);
//        typeDefnBodyAST.setInitBlock(initBlock);

        List<SpaceParser.VariableDefnStmtContext> varDefCtxts = spaceTypeDefnBodyContext.variableDefnStmt();
        if (varDefCtxts != null && !varDefCtxts.isEmpty()) {
            for (SpaceParser.VariableDefnStmtContext variableDefnStmtContext : varDefCtxts) {
                // Add var def
                VariableDefn variableDefnAST = toAst(variableDefnStmtContext.variableDefn());
                typeDefnBodyAST.addVariable(variableDefnAST);
                // Add assignment if there is one
                extractInit(typeDefnBodyAST, variableDefnStmtContext);
            }
        }

        List<SpaceParser.AssociationDefnStmtContext> assocDefCtxts = spaceTypeDefnBodyContext.associationDefnStmt();
        if (assocDefCtxts != null && !assocDefCtxts.isEmpty()) {
            for (SpaceParser.AssociationDefnStmtContext assocDefCtx : assocDefCtxts) {
                // Add declarative element
                typeDefnBodyAST.addAssocDefn(toAst(assocDefCtx.associationDefn()));
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
                    toSpacePathExpr(assocDefCtx.associationDefn().associationDecl().identifier()),
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
            blockAST.addExpr(
                toAst(astFactory.newSpacePathExpr(Antrl2AstMapping.toAst(srcFile, identifierCtxt),
                                                  null,
                                                  toText(identifierCtxt),
                                                  null),
                      rightAssignmentExprContext)
            );
        }
    }

    private AssignmentExpr toAst(SpacePathExpr memberPath,
                                 SpaceParser.RightAssignmentExprContext rightAssignmentExprContext)
    {
        logTrans(rightAssignmentExprContext);
        AssignmentExpr assignmentExprAST =
            astFactory.newAssignmentExpr(Antrl2AstMapping.toAst(srcFile, rightAssignmentExprContext));
        //
        assignmentExprAST.setMemberRef(memberPath);
        //
        assignmentExprAST.setValueExpr(toAst(rightAssignmentExprContext.valueExpr()));
        return assignmentExprAST;
    }

    private AssociationDefn toAst(SpaceParser.AssociationDefnContext assocDefCtx) {
        logTrans(assocDefCtx);
        return astFactory.newAssociationDefn(
            Antrl2AstMapping.toAst(srcFile, assocDefCtx),
            toText(assocDefCtx.associationDecl().identifier()),
            toAst(assocDefCtx.associationDecl().spacePathExpr())
        );
    }

    private SpacePathExpr toAst(SpaceParser.SpacePathExprContext spacePathExprContext) {
        logTrans(spacePathExprContext);
        if (spacePathExprContext == null)
            return null;
//        spacePathExprContext.identifier();
//        spacePathExprContext.SPathNavAssocToOper();
//        spacePathExprContext.SPathNavAssocToOper2();
        return astFactory.newSpacePathExpr(
            Antrl2AstMapping.toAst(srcFile, spacePathExprContext.identifier()),
            PathOperEnum.ASSOC_NAV,
            toText(spacePathExprContext.identifier()),
            toAst(spacePathExprContext.spacePathExpr())
        );
    }

    private AbstractFunctionDefn toAst(SpaceParser.FunctionDefnContext functionDefnContext) {
        logTrans(functionDefnContext);
        SourceInfo sourceInfo = Antrl2AstMapping.toAst(srcFile, functionDefnContext);
        FunctionDefn functionDefnAST =
            astFactory.newSpaceFunctionDefn(sourceInfo, toText(functionDefnContext.identifier()),
                                            toAst(functionDefnContext.anyTypeRef()));
        functionDefnAST.setArgSpaceTypeDefn(toAst(functionDefnContext.parameterDefnList()));
        //
        SpaceParser.StatementBlockContext statementBlockContext = functionDefnContext.statementBlock();
        functionDefnAST.setStatementBlock(toAst(statementBlockContext));
        //
        List<SpaceParser.VariableDefnStmtContext> variableDefnContexts = statementBlockContext.variableDefnStmt();
        for (SpaceParser.VariableDefnStmtContext variableDefnStmtContext : variableDefnContexts) {
            functionDefnAST.getStatementBlock().addVariable(toAst(variableDefnStmtContext.variableDefn()));
        }
        List<SpaceParser.AssociationDefnStmtContext> associationDefnContexts = statementBlockContext.associationDefnStmt();
        for (SpaceParser.AssociationDefnStmtContext associationDefnStmtContext : associationDefnContexts) {
            functionDefnAST.getStatementBlock().addAssocDefn(toAst(associationDefnStmtContext.associationDefn()));
        }

        return functionDefnAST;
    }

    private SpacePathExpr toAst(SpaceParser.AnyTypeRefContext anyTypeRefContext) {
        logTrans(anyTypeRefContext);
        SpacePathExpr pathExpr = null;
        if (anyTypeRefContext.spacePathExpr() != null) {
            pathExpr = toAst(anyTypeRefContext.spacePathExpr());
        }
        else if (anyTypeRefContext.primitiveTypeName() != null) {
            pathExpr =
                astFactory.newSpacePathExpr(Antrl2AstMapping.toAst(srcFile, anyTypeRefContext.primitiveTypeName()),
                                            null, anyTypeRefContext.primitiveTypeName().getText(), null);
        }
        else if (anyTypeRefContext.voidTypeName() != null) {
            pathExpr =
                astFactory.newSpacePathExpr(Antrl2AstMapping.toAst(srcFile, anyTypeRefContext.voidTypeName()),
                                            null, anyTypeRefContext.voidTypeName().getText(), null);
        }
        return pathExpr;
    }

    private SpaceTypeDefn toAst(SpaceParser.ParameterDefnListContext parameterDeclCtxt) {
        logTrans(parameterDeclCtxt);
        SourceInfo sourceInfo = Antrl2AstMapping.toAst(srcFile, parameterDeclCtxt);
        SpaceTypeDefn typeDefn = astFactory.newSpaceTypeDefn(sourceInfo, "func args");
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

    private AssociationDefn toAst(SpaceParser.AssociationDeclContext assocDeclCtxt) {
        logTrans(assocDeclCtxt);
        return astFactory.newAssociationDefn(
            Antrl2AstMapping.toAst(srcFile, assocDeclCtxt),
            toText(assocDeclCtxt.identifier()),
            toAst(assocDeclCtxt.spacePathExpr())
        );
    }

    private StatementBlock toAst(SpaceParser.StatementBlockContext statementBlockContext) {
        logTrans(statementBlockContext);
        StatementBlock statementBlockAST =
            astFactory.newStatementBlock(Antrl2AstMapping.toAst(srcFile, statementBlockContext));
        List<SpaceParser.VariableDefnStmtContext> variableDefnStmtContexts = statementBlockContext.variableDefnStmt();
        List<SpaceParser.AssociationDefnStmtContext> associationDefnStmtContexts = statementBlockContext.associationDefnStmt();
        List<SpaceParser.StatementContext> statementContexts = statementBlockContext.statement();
        //
        for (SpaceParser.VariableDefnStmtContext variableDefnStmtContext : variableDefnStmtContexts) {
            statementBlockAST.addVariable(toAst(variableDefnStmtContext.variableDefn()));
            extractInit(statementBlockAST, variableDefnStmtContext);
        }
        //
        for (SpaceParser.AssociationDefnStmtContext associationDefnStmtContext : associationDefnStmtContexts) {
            statementBlockAST.addAssocDefn(toAst(associationDefnStmtContext.associationDefn()));
            extractInit(statementBlockAST, associationDefnStmtContext);
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
        ReturnExpr expr = astFactory.newReturnExpr(Antrl2AstMapping.toAst(srcFile, returnStatementContext),
                                                   toAst(returnStatementContext.valueExpr()));
        return expr;
    }

    private ValueExpr toAst(SpaceParser.ExpressionContext expressionCtxt) {
        logTrans(expressionCtxt);
        ValueExpr valueExprAST = null;
        if (expressionCtxt.variableDefn() != null) {
            // creates just the assignment expr
            valueExprAST = toAst(toSpacePathExpr(expressionCtxt.variableDefn().variableDecl().identifier()),
                                 expressionCtxt.variableDefn().rightAssignmentExpr());
        }
        else if (expressionCtxt.associationDefn() != null) {
            // creates just the assignment expr
            valueExprAST =
                toAst(toSpacePathExpr(expressionCtxt.associationDefn().associationDecl().identifier()),
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
        return astFactory.newOperatorExpr(Antrl2AstMapping.toAst(srcFile, unaryOperExprContext),
                                          toAst(unaryOperExprContext.unaryOper()),
                                          toAst(unaryOperExprContext.valueExpr()));
    }

    private OperEnum toAst(SpaceParser.UnaryOperContext unaryOperContext) {
        return toAstOper(unaryOperContext.BooleanUnaryOper());
    }

    private ValueExpr toAst(SpaceParser.BinaryOperExprContext binaryOperExprContext) {
        logTrans(binaryOperExprContext);
        return astFactory.newOperatorExpr(Antrl2AstMapping.toAst(srcFile, binaryOperExprContext),
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

    private Map<String, OperEnum> operSymbolMap = new TreeMap();

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

    private OperEnum toAstOper(TerminalNode terminalNode) {
        OperEnum operEnumAST = operSymbolMap.get(terminalNode.getSymbol().getText());
        return operEnumAST;
    }

    private SpacePathExpr toSpacePathExpr(SpaceParser.IdentifierContext identifierCtxt) {
        logTrans(identifierCtxt);
        return astFactory.newSpacePathExpr(Antrl2AstMapping.toAst(srcFile, identifierCtxt),
                                           null,
                                           toText(identifierCtxt),
                                           null);
    }

    private AssignmentExpr toAst(SpaceParser.AssignmentExprContext assignmentExprContext) {
        return toAst(
            toAst(assignmentExprContext.spacePathExpr()),
            assignmentExprContext.rightAssignmentExpr()
        );
    }

    private FunctionCallExpr toAst(SpaceParser.FunctionCallExprContext functionCallExprContext) {
        logTrans(functionCallExprContext);
        FunctionCallExpr functionCallExpr =
            astFactory.newFunctionCallExpr(Antrl2AstMapping.toAst(srcFile, functionCallExprContext));
        //
        functionCallExpr.setFunctionDefnRef(toAst(functionCallExprContext.spacePathExpr()));
        //
        if (functionCallExprContext.argTupleOrRef() != null) {
            SpaceParser.TupleLiteralContext tupleLiteralCtxt =
                functionCallExprContext.argTupleOrRef().tupleLiteral();
            SpaceParser.SpacePathExprContext spacePathExprCtxt =
                functionCallExprContext.argTupleOrRef().spacePathExpr();
            List<ValueExpr> thisCallArgs = new LinkedList<>();
            if (tupleLiteralCtxt != null) {
                SpaceParser.ValueOrAssignmentExprListContext callArgExprCtxts =
                    tupleLiteralCtxt.valueOrAssignmentExprList();
                int idxArg = 0;
                for (SpaceParser.ValueOrAssignmentExprContext callArgContext : callArgExprCtxts.valueOrAssignmentExpr())
                {
                    ValueExpr rightSide = toAst(callArgContext);
                    thisCallArgs.add(rightSide);
                    // TODO Don't assume ordered arg list
                    idxArg++;
                }
            }

            functionCallExpr.setArgumentExprs(thisCallArgs);
        }
        return functionCallExpr;
    }

    /** Then general translator from ANTLR expression to AST expression.
     *  A bit complex since ANTRL trees provide no polymorphism, per se.
     */
    private ValueExpr toAst(SpaceParser.ValueOrAssignmentExprContext exprContext) {
        logTrans(exprContext);
        ValueExpr rightSide = null;
        SpaceParser.ValueExprContext valueExprContext = exprContext.valueExpr();
        rightSide = toAst(valueExprContext);
        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ValueExprContext valueExprContext) {
        logTrans(valueExprContext);
        ValueExpr valueExprAST = null;
        if (valueExprContext != null) {
            // choices ...
            SpaceParser.LiteralExprContext literalExprContext = valueExprContext.literalExpr();
            SpaceParser.FunctionCallExprContext functionCallExprContext = valueExprContext.functionCallExpr();
            SpaceParser.ObjectExprContext objectExprContext = valueExprContext.objectExpr();
            SpaceParser.SpacePathExprContext spacePathExprContext = valueExprContext.spacePathExpr();
            SpaceParser.OperatorExprContext operatorExprContext = valueExprContext.operatorExpr();
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
                valueExprAST = new MetaReference(toAst(spacePathExprContext), MetaType.DATUM);
            }
            else if (operatorExprContext != null) {
                valueExprAST = toAst(operatorExprContext);
            }
        }
        return valueExprAST;
    }

    private ValueExpr toAst(SpaceParser.ObjectExprContext objectExprContext) {
        return null;
    }

    /**
     * Character string literals (CharactersSequences) produce, in effect, a 'new CharacterSequence("")'
     * call. */
    private SequenceLiteralExpr toAst(SpaceParser.StringLiteralContext stringLiteralContext) {
        logTrans(stringLiteralContext);
        SequenceLiteralExpr primitiveLiteralExpr = null;

        primitiveLiteralExpr = astFactory.newSequenceLiteralExpr(
            Antrl2AstMapping.toAst(srcFile, stringLiteralContext),
            stringLiteralContext.StringLiteral().getText()
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
        return astFactory.newPrimLiteralExpr(Antrl2AstMapping.toAst(srcFile, floatLiteralCtxt), PrimitiveTypeDefn.REAL,
                                             floatLiteralCtxt.FloatLiteral().getSymbol().getText());
    }

    private PrimitiveLiteralExpr toAst(SpaceParser.BooleanLiteralContext boolLiteralCtxt) {
        logTrans(boolLiteralCtxt);
        return astFactory.newPrimLiteralExpr(Antrl2AstMapping.toAst(srcFile, boolLiteralCtxt), PrimitiveTypeDefn.BOOLEAN,
                                             boolLiteralCtxt.BooleanLiteral().getSymbol().getText());
    }

    private PrimitiveLiteralExpr toAst(SpaceParser.IntegerLiteralContext intLiteralCtxt) {
        logTrans(intLiteralCtxt);
        return astFactory.newPrimLiteralExpr(Antrl2AstMapping.toAst(srcFile, intLiteralCtxt), PrimitiveTypeDefn.CARD,
                                             intLiteralCtxt.IntegerLiteral().getSymbol().getText());
    }

    private VariableDefn toAst(SpaceParser.VariableDefnContext variableDefnContext) {
        logTrans(variableDefnContext);
        SpaceParser.VariableDeclContext varDeclCtxt = variableDefnContext.variableDecl();
        VariableDefn element = toAst(varDeclCtxt);
        // TODO handle variableDefnContext.rightAssignmentExpr();
        return element;
    }

    private VariableDefn toAst(SpaceParser.VariableDeclContext varDeclCtxt)
    {
        logTrans(varDeclCtxt);
        return astFactory.newVariableDefn(
            Antrl2AstMapping.toAst(srcFile, varDeclCtxt),
            toText(varDeclCtxt.identifier()),
            toAst(varDeclCtxt.primitiveTypeName())
        );
    }

    private PrimitiveTypeDefn toAst(SpaceParser.PrimitiveTypeNameContext primitiveTypeNameContext) {
        logTrans(primitiveTypeNameContext);
        return PrimitiveTypeDefn.valueOf(primitiveTypeNameContext.getText());
    }

    private void logTrans(ParserRuleContext antlrRuleCtxt) {
        if (log.isDebugEnabled() && antlrRuleCtxt != null)
            log.debug("transforming " + antlrRuleCtxt.getClass().getSimpleName() + " at " +
                          Antrl2AstMapping.toAst(srcFile, antlrRuleCtxt));
    }

    private String toText(SpaceParser.IdentifierContext identifierContext) {
        return identifierContext.Identifier().getSymbol().getText();
    }

//    private String[] toNsArray(List<SpaceParser.IdentifierContext> fqNsIds) {
//        String[] nsArray = new String[fqNsIds.size()];
//        for (int idxId = 0; idxId < fqNsIds.size(); idxId++) {
//            nsArray[idxId] = toText(fqNsIds.get(idxId));
//        }
//        return nsArray;
//    }

    private interface Transformer {
        ModelElement trans(ParseTree parseNode);
    }
}
