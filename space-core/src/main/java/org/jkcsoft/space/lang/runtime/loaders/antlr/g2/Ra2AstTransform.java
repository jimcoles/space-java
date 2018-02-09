/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr.g2;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.runtime.ExecState;

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

    public static final ActionCallExpr NAV_OPER = asCall("space", "nav");

    private interface Transformer {
        ModelElement trans(ParseTree parseNode);
    }

    private Map<Class<? extends ParseTree>, Transformer> transformerMap = new HashMap<>();

    private static ActionCallExpr asCall(String ... paths) {
        return null;
    }

    private ExecState state = ExecState.INITIALIZATION;
    private List<AstLoadError> errors = new LinkedList();
    private AstBuilder astBuilder;
//    private ObjectBuilder objBuilder = ObjectBuilder.getInstance();


    public Ra2AstTransform() {
    }

    public AstBuilder transformAndBuild(ParseTree parseTreeRoot) {

//        visitNodeRecurse(parseTreeRoot);

        return astBuilder;
    }

    private ModelElement lookupTrans(ParseTree node) {
        ModelElement trans = transformerMap.get(node.getClass()).trans(node);
        return trans;
    }

    public AstBuilder transform(SpaceParser.ParseUnitContext spaceParseUnit) {
        log.info("transforming ANTLR parse tree to AST starting with " + SpaceParser.ParseUnitContext.class.getSimpleName());
        astBuilder = new AstBuilder();
        astBuilder.initProgram("");
        astBuilder.getAstRoot().addSpaceDefn(typeDef2Ast(spaceParseUnit.spaceTypeDefn()));
        return astBuilder;
    }

    public SpaceTypeDefn typeDef2Ast(SpaceParser.SpaceTypeDefnContext spaceTypeDefnContext) {
        log.info("transforming " + SpaceParser.SpaceTypeDefnContext.class.getSimpleName());

        SpaceTypeDefn spaceTypeDefn = null;
        // TODO: 2/8/17 Not all spaces are Entities?
        spaceTypeDefn = astBuilder.newSpaceTypeDefn(toText(spaceTypeDefnContext.identifier()));
        spaceTypeDefnContext.accessModifier();
        spaceTypeDefnContext.defnTypeModifier();
        spaceTypeDefnContext.elementDefnHeader();
        if (spaceTypeDefnContext.ExtendsKeyword() != null) {
            //            spaceTypeDefnContext.spacePathList();
        }
        SpaceParser.SpaceTypeDefnBodyContext spaceTypeDefnBodyContext = spaceTypeDefnContext.spaceTypeDefnBody();

        List<SpaceParser.VariableDefnStmntContext> varDefCtxts = spaceTypeDefnBodyContext.variableDefnStmnt();
        if (varDefCtxts != null && !varDefCtxts.isEmpty()) {
            for (SpaceParser.VariableDefnStmntContext variableDefnStmntContext : varDefCtxts) {
                spaceTypeDefn.addVariable(toAst(variableDefnStmntContext.variableDefn()));
            }
        }

        List<SpaceParser.AssociationDefnStmntContext> assocDefCtxts = spaceTypeDefnBodyContext.associationDefnStmnt();
        if (assocDefCtxts != null && !assocDefCtxts.isEmpty()) {
            for (SpaceParser.AssociationDefnStmntContext assocDefCtx : assocDefCtxts) {
                spaceTypeDefn.addAssociation(toAst(assocDefCtx.associationDefn()));
            }
        }

        List<SpaceParser.ActionDefnContext> actionDefCtxts = spaceTypeDefnBodyContext.actionDefn();
        if (actionDefCtxts != null && !actionDefCtxts.isEmpty()) {
            for (SpaceParser.ActionDefnContext actionDefnCtx : actionDefCtxts) {
                spaceTypeDefn.addActionDefn(toAst(actionDefnCtx));
            }
        }

        List<SpaceParser.SpaceTypeDefnContext> childSpaceTypeDefnCtxts = spaceTypeDefnBodyContext.spaceTypeDefn();
        // TODO Handle sub-space defn

//        errors.add(new AstLoadError("Space body element was not one of the three kinds expected.",
//                bodyElement.getStart().getLine(),
//                bodyElement.getStart().getCharPositionInLine()));

        return spaceTypeDefn;
    }

    private AssociationDefn toAst(SpaceParser.AssociationDefnContext assocDefCtx) {
        assocDefCtx.rightAssignmentExpr();
        assocDefCtx.getSourceInterval();
        return astBuilder.newAssociationDefn(
                toText(assocDefCtx.associationDecl().identifier()),

                toAst(assocDefCtx.associationDecl().spacePathExpr())
        );
    }

    private SpacePathExpr toAst(SpaceParser.SpacePathExprContext spacePathExprContext) {
        spacePathExprContext.identifier();
        spacePathExprContext.SPathNavAssocToOper();
        spacePathExprContext.SPathNavAssocToOper2();
        return astBuilder.newSpacePathExpr(
                PathOperEnum.ASSOC_NAV,
                toText(spacePathExprContext.identifier())
        );
    }

    private AbstractActionDefn toAst(SpaceParser.ActionDefnContext actionDefnContext) {
        log.info("transforming " + SpaceParser.ActionDefnContext.class.getSimpleName());

        SpaceActionDefn actionDefn
            = astBuilder.newSpaceActionDefn(toText(actionDefnContext.identifier()));

        List<SpaceParser.StatementContext> statements = actionDefnContext.actionDefnBody().statement();
        for (SpaceParser.StatementContext statement : statements) {
            if (statement.expression().actionCallExpr() != null) {
                actionDefn.addAction(toAst(statement.expression().actionCallExpr()));
            }
            else if (statement.expression().assignmentExpr() != null) {
                actionDefn.addAssignment(toAst(statement.expression().assignmentExpr()));
            }
        }

//        actionDefnContext.anyTypeRef();
        return actionDefn;
    }

    /** Transforms an assignment expression to a special kind of {@link ActionCallExpr}. */
    private ActionCallExpr toAst(SpaceParser.AssignmentExprContext assignmentExprContext) {
        return null;  // TODO impl
    }

    private ActionCallExpr toAst(SpaceParser.ActionCallExprContext actionCallExprContext) {
        SpacePathExpr spacePathExpr = toAst(actionCallExprContext.spacePathExpr());

        SpaceParser.ValueOrAssignmentExprListContext callArgExprCtxts =
                actionCallExprContext.valueOrAssignmentExprList();
        ValueExpr[] thisCallArgs = new ValueExpr[callArgExprCtxts.valueOrAssignmentExpr().size()];
        int idxArg = 0;
        for (SpaceParser.ValueOrAssignmentExprContext callArgContext : callArgExprCtxts.valueOrAssignmentExpr()) {
            ValueExpr rightSide = toAstValueExpr(callArgContext);
            thisCallArgs[idxArg] = rightSide;
            idxArg++;
        }

        return astBuilder.newActionCallExpr("callPoint", spacePathExpr, thisCallArgs);
    }

    /** Then general translator from ANTLR expression to AST expression.
     *  A bit complex since ANTRL trees provide no polymorphism, per se.
     */
    private ValueExpr toAstValueExpr(SpaceParser.ValueOrAssignmentExprContext exprContext) {
        ValueExpr rightSide = null;
        SpaceParser.ValueExprContext valueExprContext = exprContext.valueExpr();
        if (valueExprContext != null) {
            SpaceParser.LiteralExprContext literalExprContext = valueExprContext.literalExpr();
            SpaceParser.ActionCallExprContext actionCallExprContext = valueExprContext.actionCallExpr();
            SpaceParser.ObjectExprContext objectExprContext = valueExprContext.objectExpr();
            SpaceParser.SpacePathExprContext spacePathExprContext = valueExprContext.spacePathExpr();
            if (literalExprContext != null) {
                if (literalExprContext.scalarLiteral() != null)
                    rightSide = toAst(literalExprContext.scalarLiteral());
                else if (literalExprContext.stringLiteral() != null)
                    rightSide = toAst(literalExprContext.stringLiteral());
            }
            else if (objectExprContext != null)
                rightSide = toAst(objectExprContext);
            else if (actionCallExprContext != null)
                // nested
                rightSide = toAst(actionCallExprContext);
            else if (spacePathExprContext != null) {
                rightSide = toAstCall(spacePathExprContext);
            }
        }
        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ObjectExprContext objectExprContext) {
        return null;
    }

    /** Transforms parse tree Space Path Expr to a nested list of nav operations. */
    private ActionCallExpr toAstCall(SpaceParser.SpacePathExprContext spacePathExprContext) {
        ActionCallExpr actionCallExpr = null;
//        spacePathExprContext.spacePathExpr();
//        spacePathExprContext.identifier();
        ActionCallExpr nextCallExpr = null;
        if (spacePathExprContext.spacePathExpr() != null) {
            nextCallExpr = toAstCall(spacePathExprContext.spacePathExpr());
        }
//        ActionCallExpr actionCallExpr =
//            astBuilder.newActionCallExpr("callPoint",
//                                         OperEnum.ASSOC_NAV,
//                                         nextCallExpr
//                                         );
//            nextCallExpr.addChild(actionCallExpr);
        return actionCallExpr;
    }

    private ActionCallExpr toAst(List<SpaceParser.SpacePathExprContext> spacePathExprContexts) {
        return null;
    }

    /**
     * Character string literals (CharactersSequences) produce, in effect, a 'new CharacterSequence("")'
     * call. */
    private ValueExpr toAst(SpaceParser.StringLiteralContext stringLiteralContext) {
        ValueExpr rightSide = null;

//        CharacterSequence arg1 = objBuilder.newCharacterSequence(
//            stringLiteralContext.StringLiteral().getText()
//        );

        rightSide = astBuilder.newLiteralHolder(stringLiteralContext.StringLiteral().getText());

//        objBuilder.newObjectReference(
//            astBuilder.newAssociationDefn("arg1", null, null),
//            arg1.getOid()
//        );

        return rightSide;
    }

    private ValueExpr toAst(SpaceParser.ScalarLiteralContext scalarLiteralContext) {
        return astBuilder.newLiteralHolder(scalarLiteralContext.integerLiteral().getText());
    }

    private List<SpaceParser.IdentifierContext> collapseList(SpaceParser.SpacePathExprContext identifierRefContext) {
        List<SpaceParser.IdentifierContext> listOfIds = new LinkedList<>();
        listOfIds.add(identifierRefContext.identifier());
        return listOfIds;
    }

    private VariableDefn toAst(SpaceParser.VariableDefnContext variableDefnContext) {
        log.info("transforming " + SpaceParser.VariableDefnContext.class.getSimpleName());
        VariableDefn element = astBuilder.newVariableDefn(
                toText(variableDefnContext.variableDecl().identifier()),
                toAst(variableDefnContext.variableDecl().primitiveTypeName())
        );
        return element;
    }

    private PrimitiveType toAst(SpaceParser.PrimitiveTypeNameContext primitiveTypeNameContext) {
        log.info("transforming " + SpaceParser.PrimitiveTypeNameContext.class.getSimpleName()
        + "=" + primitiveTypeNameContext.getText());

        return PrimitiveType.valueOf(primitiveTypeNameContext.getText().toUpperCase());
    }

    private String[] toNsArray(List<SpaceParser.IdentifierContext> fqNsIds) {
        String[] nsArray = new String[fqNsIds.size()];
        for (int idxId = 0; idxId < fqNsIds.size(); idxId++) {
            nsArray[idxId] = toText(fqNsIds.get(idxId));
        }
        return nsArray;
    }

    private String toText(SpaceParser.IdentifierContext identifierContext) {
        return identifierContext.Identifier().getSymbol().getText();
    }
}
