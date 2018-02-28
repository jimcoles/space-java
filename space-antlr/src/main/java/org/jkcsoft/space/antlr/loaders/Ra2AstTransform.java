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

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.runtime.ExecState;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public static final FunctionCallExpr NAV_OPER = asCall("space", "nav");
    private static FunctionCallExpr asCall(String ... paths) {
        return null;
    }

    private interface Transformer {
        ModelElement trans(ParseTree parseNode);
    }

    // -------------------------------------------------------------------------
    //
    private Map<Class<? extends ParseTree>, Transformer> transformerMap = new HashMap<>();
    private ExecState state = ExecState.INITIALIZED;
    private List<AstLoadError> errors = new LinkedList();
    private AstFactory astFactory;
    private File srcFile;

//    private ObjectFactory objBuilder = ObjectFactory.getInstance();


    public Ra2AstTransform(AstFactory astFactory, File srcFile) {
        this.astFactory = astFactory;
        this.srcFile = srcFile;
    }

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
        Schema schema = astFactory.newAstSchema(Antrl2AstMapping.toAst(srcFile, spaceParseUnit), "user");
        schema.addSpaceDefn(toAst(spaceParseUnit.spaceTypeDefn()));
        return schema;
    }

    public SpaceTypeDefn toAst(SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext) {
        log.info("transforming " + SpaceParser.SpaceTypeDefnContext.class.getSimpleName());

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


//        errors.add(new AstLoadError("Space body element was not one of the three kinds expected.",
//                bodyElement.getStart().getLine(),
//                bodyElement.getStart().getCharPositionInLine()));

        return spaceTypeDefn;
    }

    private SpaceTypeDefnBody toAst(SpaceParser.SpaceTypeDefnBodyContext spaceTypeDefnBodyContext) {

        SourceInfo bodySourceInfo = Antrl2AstMapping.toAst(srcFile, spaceTypeDefnBodyContext);
        SpaceTypeDefnBody typeDefnBodyAST =
            astFactory.newTypeDefnBody(bodySourceInfo);

        StatementBlock initBlock = astFactory.newStatementBlock(bodySourceInfo);
        typeDefnBodyAST.setInitBlock(initBlock);

        List<SpaceParser.VariableDefnStmtContext> varDefCtxts = spaceTypeDefnBodyContext.variableDefnStmt();
        if (varDefCtxts != null && !varDefCtxts.isEmpty()) {
            for (SpaceParser.VariableDefnStmtContext variableDefnStmtContext : varDefCtxts) {
                // add var def
                VariableDefn variableDefnAST = toAst(variableDefnStmtContext.variableDefn());
                typeDefnBodyAST.addVariable(variableDefnAST);
                // add assignment if there is one
                SpaceParser.IdentifierContext identifierCtxt =
                    variableDefnStmtContext.variableDefn().variableDecl().identifier();
                SpaceParser.RightAssignmentExprContext rightAssignmentExprContext =
                    variableDefnStmtContext.variableDefn().rightAssignmentExpr();
                if (rightAssignmentExprContext != null) {
                    initBlock.addExpr(
                        toAst(astFactory.newSpacePathExpr(Antrl2AstMapping.toAst(srcFile, identifierCtxt), null,
                                                          toText(identifierCtxt), null),
                              rightAssignmentExprContext)
                    );
                }
            }
        }

        List<SpaceParser.AssociationDefnStmtContext> assocDefCtxts = spaceTypeDefnBodyContext.associationDefnStmt();
        if (assocDefCtxts != null && !assocDefCtxts.isEmpty()) {
            for (SpaceParser.AssociationDefnStmtContext assocDefCtx : assocDefCtxts) {
                typeDefnBodyAST.addAssocDefn(toAst(assocDefCtx.associationDefn()));
                SpaceParser.IdentifierContext identifierCtxt =
                    assocDefCtx.associationDefn().associationDecl().identifier();
                SpaceParser.RightAssignmentExprContext rightAssignmentExprContext =
                    assocDefCtx.associationDefn().rightAssignmentExpr();
                if (rightAssignmentExprContext != null) {
                    initBlock.addExpr(
                        toAst(astFactory.newSpacePathExpr(Antrl2AstMapping.toAst(srcFile, identifierCtxt), null,
                                                          toText(identifierCtxt), null),
                              rightAssignmentExprContext)
                    );

                }
            }
        }

        List<SpaceParser.ActionDefnContext> actionDefCtxts = spaceTypeDefnBodyContext.actionDefn();
        if (actionDefCtxts != null && !actionDefCtxts.isEmpty()) {
            for (SpaceParser.ActionDefnContext actionDefnCtx : actionDefCtxts) {
                typeDefnBodyAST.addFunctionDefn(toAst(actionDefnCtx));
            }
        }

//        List<SpaceParser.SpaceTypeDefnContext> childSpaceTypeDefnCtxts = spaceTypeDefnBodyContext.spaceTypeDefn();
        // TODO Handle sub-space defn

        return typeDefnBodyAST;
    }

    private AssignmentExpr toAst(SpacePathExpr memberPath, SpaceParser.RightAssignmentExprContext rightAssignmentExprContext) {
        AssignmentExpr assignmentExprAST =
            astFactory.newAssignmentExpr(Antrl2AstMapping.toAst(srcFile, rightAssignmentExprContext));
        //
        assignmentExprAST.setMemberRef(memberPath);
        //
        assignmentExprAST.setValueExpr(toAst(rightAssignmentExprContext.valueExpr()));
        return assignmentExprAST;
    }

    private AssociationDefn toAst(SpaceParser.AssociationDefnContext assocDefCtx) {
        return astFactory.newAssociationDefn(
            Antrl2AstMapping.toAst(srcFile, assocDefCtx),
            toText(assocDefCtx.associationDecl().identifier()),
            toAst(assocDefCtx.associationDecl().spacePathExpr())
        );
    }

    private SpacePathExpr toAst(SpaceParser.SpacePathExprContext spacePathExprContext) {
        if (spacePathExprContext == null)
            return null;

        spacePathExprContext.identifier();
        spacePathExprContext.SPathNavAssocToOper();
        spacePathExprContext.SPathNavAssocToOper2();
        return astFactory.newSpacePathExpr(
                Antrl2AstMapping.toAst(srcFile, spacePathExprContext),
                PathOperEnum.ASSOC_NAV,
                toText(spacePathExprContext.identifier()),
                toAst(spacePathExprContext.spacePathExpr())
        );
    }

    private AbstractFunctionDefn toAst(SpaceParser.ActionDefnContext actionDefnContext) {
        log.info("transforming " + SpaceParser.ActionDefnContext.class.getSimpleName());

        SourceInfo sourceInfo = Antrl2AstMapping.toAst(srcFile, actionDefnContext);
        FunctionDefn functionDefnAST =
            astFactory.newSpaceFunctionDefn(sourceInfo, toText(actionDefnContext.identifier()));
        functionDefnAST.setArgSpaceTypeDefn(toAst(actionDefnContext.parameterDefnList()));
        //
        functionDefnAST.setStatementBlock(toAst(actionDefnContext.actionDefnBody()));

        return functionDefnAST;
    }

    private SpaceTypeDefn toAst(SpaceParser.ParameterDefnListContext parameterDeclCtxt) {
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
        return astFactory.newAssociationDefn(
            Antrl2AstMapping.toAst(srcFile, assocDeclCtxt),
            toText(assocDeclCtxt.identifier()),
            toAst(assocDeclCtxt.spacePathExpr())
        );
    }


    private StatementBlock toAst(SpaceParser.ActionDefnBodyContext actionDefnBodyContext) {
        StatementBlock statementBlockAST =
            astFactory.newStatementBlock(Antrl2AstMapping.toAst(srcFile, actionDefnBodyContext));
        List<SpaceParser.StatementContext> statements = actionDefnBodyContext.statement();
        for (SpaceParser.StatementContext statementPO : statements) {
            if (statementPO.expression().actionCallExpr() != null) {
                statementBlockAST.addExpr(toAst(statementPO.expression().actionCallExpr()));
            }
            else if (statementPO.expression().assignmentExpr() != null) {
                statementBlockAST.addExpr(toAst(statementPO.expression().assignmentExpr()));
            }
        }
        return statementBlockAST;
    }

    private AssignmentExpr toAst(SpaceParser.AssignmentExprContext assignmentExprContext) {
        return toAst(toAst(assignmentExprContext.spacePathExpr()),
                     assignmentExprContext.rightAssignmentExpr());
    }

    private FunctionCallExpr toAst(SpaceParser.ActionCallExprContext actionCallExprContext) {
        FunctionCallExpr functionCallExpr =
            astFactory.newFunctionCallExpr(Antrl2AstMapping.toAst(srcFile, actionCallExprContext));
        //
        functionCallExpr.setFunctionRef(toAst(actionCallExprContext.spacePathExpr()));
        //
        SpaceParser.ValueOrAssignmentExprListContext callArgExprCtxts =
                actionCallExprContext.valueOrAssignmentExprList();
        ValueExpr[] thisCallArgs = new ValueExpr[callArgExprCtxts.valueOrAssignmentExpr().size()];
        int idxArg = 0;
        for (SpaceParser.ValueOrAssignmentExprContext callArgContext : callArgExprCtxts.valueOrAssignmentExpr()) {
            ValueExpr rightSide = toAst(callArgContext);
            thisCallArgs[idxArg] = rightSide;
            idxArg++;
        }

        functionCallExpr.setArgumentExprs(thisCallArgs);

        return functionCallExpr;
    }

    /** Then general translator from ANTLR expression to AST expression.
     *  A bit complex since ANTRL trees provide no polymorphism, per se.
     */
    private ValueExpr toAst(SpaceParser.ValueOrAssignmentExprContext exprContext) {
        ValueExpr rightSide = null;
        SpaceParser.ValueExprContext valueExprContext = exprContext.valueExpr();
        rightSide = toAst(valueExprContext);
        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ValueExprContext valueExprContext) {
        ValueExpr valueExprAST = null;
        if (valueExprContext != null) {
            // choices ...
            SpaceParser.LiteralExprContext literalExprContext = valueExprContext.literalExpr();
            SpaceParser.ActionCallExprContext actionCallExprContext = valueExprContext.actionCallExpr();
            SpaceParser.ObjectExprContext objectExprContext = valueExprContext.objectExpr();
            SpaceParser.SpacePathExprContext spacePathExprContext = valueExprContext.spacePathExpr();
            if (literalExprContext != null) {
                if (literalExprContext.scalarLiteral() != null)
                    valueExprAST = toAst(literalExprContext.scalarLiteral());
                else if (literalExprContext.stringLiteral() != null)
                    valueExprAST = toAst(literalExprContext.stringLiteral());
            }
            else if (objectExprContext != null)
                valueExprAST = toAst(objectExprContext);
            else if (actionCallExprContext != null)
                // nested
                valueExprAST = toAst(actionCallExprContext);
            else if (spacePathExprContext != null) {
                valueExprAST = new MetaReference(toAst(spacePathExprContext));
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
    private LiteralExpr toAst(SpaceParser.StringLiteralContext stringLiteralContext) {
        LiteralExpr literalExpr = null;

        literalExpr = astFactory.newLiteralHolder(
            Antrl2AstMapping.toAst(srcFile, stringLiteralContext),
            PrimitiveType.TEXT,
            stringLiteralContext.StringLiteral().getText()
        );

        return literalExpr;
    }

    private ValueExpr toAst(SpaceParser.ScalarLiteralContext scalarLiteralContext) {
        ValueExpr element = null;
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
            throw new IllegalArgumentException("don't yet know how to translate literal expression " + scalarLiteralContext);
        
        return element;
    }

    private LiteralExpr toAst(SpaceParser.FloatLiteralContext floatLiteralCtxt) {
        return astFactory.newLiteralHolder(Antrl2AstMapping.toAst(srcFile, floatLiteralCtxt), PrimitiveType.REAL,
                                           floatLiteralCtxt.FloatLiteral().getSymbol().getText());
    }

    private LiteralExpr toAst(SpaceParser.BooleanLiteralContext boolLiteralCtxt) {
        return astFactory.newLiteralHolder(Antrl2AstMapping.toAst(srcFile, boolLiteralCtxt), PrimitiveType.BOOLEAN,
                                           boolLiteralCtxt.BooleanLiteral().getSymbol().getText());
    }

    private LiteralExpr toAst(SpaceParser.IntegerLiteralContext intLiteralCtxt) {
        return astFactory.newLiteralHolder(Antrl2AstMapping.toAst(srcFile, intLiteralCtxt), PrimitiveType.CARD,
                                           intLiteralCtxt.IntegerLiteral().getSymbol().getText());
    }

    private VariableDefn toAst(SpaceParser.VariableDefnContext variableDefnContext) {
        log.info("transforming " + SpaceParser.VariableDefnContext.class.getSimpleName());
        SpaceParser.VariableDeclContext varDeclCtxt = variableDefnContext.variableDecl();
        VariableDefn element = toAst(varDeclCtxt);
        // TODO handle variableDefnContext.rightAssignmentExpr();
        return element;
    }

    private VariableDefn toAst(SpaceParser.VariableDeclContext varDeclCtxt)
    {
        return astFactory.newVariableDefn(
            Antrl2AstMapping.toAst(srcFile, varDeclCtxt),
            toText(varDeclCtxt.identifier()),
            toAst(varDeclCtxt.primitiveTypeName())
        );
    }

    private PrimitiveType toAst(SpaceParser.PrimitiveTypeNameContext primitiveTypeNameContext) {
        log.info("transforming " + SpaceParser.PrimitiveTypeNameContext.class.getSimpleName()
        + "=" + primitiveTypeNameContext.getText());

        return PrimitiveType.valueOf(primitiveTypeNameContext.getText().toUpperCase());
    }

//    private String[] toNsArray(List<SpaceParser.IdentifierContext> fqNsIds) {
//        String[] nsArray = new String[fqNsIds.size()];
//        for (int idxId = 0; idxId < fqNsIds.size(); idxId++) {
//            nsArray[idxId] = toText(fqNsIds.get(idxId));
//        }
//        return nsArray;
//    }

    private String toText(SpaceParser.IdentifierContext identifierContext) {
        return identifierContext.Identifier().getSymbol().getText();
    }
}
