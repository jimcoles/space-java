/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime.loaders.antlr.g2;

import org.apache.log4j.Logger;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.runtime.ExecState;

import java.util.LinkedList;
import java.util.List;

/**
 * A brute force interrogator of the ANTLR parse tree that I'll use
 * until or unless I think of a more elegant approach.  The scheme here is that
 * a top-level toAst( ) method takes an ANTLR parse tree to build top-level
 * AST notions, then recurses into child notions.
 *
 * @author Jim Coles
 */
public class Ra2AstTransform {

    private static final Logger log = Logger.getRootLogger();

    public static final ActionCallExpr NAV_OPER = asCall("space", "nav");

    private static ActionCallExpr asCall(String ... paths) {
        return null;
    }

    private ExecState state = ExecState.INITIALIZATION;
    private List<AstLoadError> errors = new LinkedList();
    private AstBuilder astBuilder;
//    private ObjectBuilder objBuilder = ObjectBuilder.getInstance();

    public AstBuilder toAst(SpaceParser.ParseUnitContext spaceParseUnit) {
        log.info("transforming " + SpaceParser.ParseUnitContext.class.getSimpleName());

        astBuilder = new AstBuilder();
        astBuilder.initProgram();
        astBuilder.getAstRoot().addSpaceDefn(toAst(spaceParseUnit.spaceDefn()));
        return astBuilder;
    }

    private SpaceDefn toAst(SpaceParser.SpaceDefnContext spaceDefnContext) {
        log.info("transforming " + SpaceParser.SpaceDefnContext.class.getSimpleName());

        SpaceDefn spaceDefn = null;
        // TODO: 2/8/17 Not all spaces are Entities?
        spaceDefn = astBuilder.newSpaceDefn(toText(spaceDefnContext.identifier()));
        spaceDefnContext.accessModifier();
        spaceDefnContext.defnTypeModifier();
        spaceDefnContext.elementDefnHeader();
        if (spaceDefnContext.ExtendsKeyword() != null) {
            spaceDefnContext.spacePathList();
        }
        List<SpaceParser.AnySpaceElementDefnContext> bodyElements
            = spaceDefnContext.spaceDefnBody().anySpaceElementDefn();
        addToAst(spaceDefn, bodyElements);
        return spaceDefn;
    }

    private void addToAst(SpaceDefn spaceDefn, List<SpaceParser.AnySpaceElementDefnContext> bodyElements) {
        for (SpaceParser.AnySpaceElementDefnContext bodyElement : bodyElements) {
            if (bodyElement.variableDefn() != null) {
                spaceDefn.addVariable(toAst(bodyElement.variableDefn()));
            }
            else if (bodyElement.associationDefn() != null) {
//                spaceDefn.addAssociationDefn(toAst(bodyElement.associationDefn()));
            }
            else if (bodyElement.actionDefn() != null) {
                spaceDefn.addActionDefn(toAst(bodyElement.actionDefn()));
            }
            else {
                errors.add(new AstLoadError("Space body element was not one of the three kinds expected.",
                                            bodyElement.getStart().getLine(),
                                            bodyElement.getStart().getCharPositionInLine()));
            }
        }
    }

    private AbstractActionDefn toAst(SpaceParser.ActionDefnContext actionDefnContext) {
        log.info("transforming " + SpaceParser.ActionDefnContext.class.getSimpleName());

        SpaceActionDefn actionDefn
            = astBuilder.newSpaceActionDefn(toText(actionDefnContext.identifier()));
        actionDefn.setArgSpaceDefn(toAst(actionDefnContext.anySpaceElementDefn()));
        List<SpaceParser.ActionCallExprContext> actionCallExprContexts
            = actionDefnContext.actionDefnBody().actionCallExpr();
        for (SpaceParser.ActionCallExprContext actionCallExprContext : actionCallExprContexts) {
            actionDefn.addAction(toAst(actionCallExprContext));
        }
//        actionDefnContext.anyTypeRef();
        return actionDefn;
    }

    private ActionCallExpr toAst(SpaceParser.ActionCallExprContext actionCallExprContext) {
        ActionCallExpr spacePathExpr = toAst(actionCallExprContext.spacePathExpr());

        List<SpaceParser.ValueExprContext> callArgContexts = actionCallExprContext.valueExpr();
        ValueExpr[] thisCallArgs = new ValueExpr[callArgContexts.size()];
        int idxArg = 0;
        for (SpaceParser.ValueExprContext callArgContext : callArgContexts) {
            ValueExpr rightSide = null;
            if (callArgContext.literalExpr() != null && callArgContext.literalExpr().scalarLiteral() != null)
                rightSide = toAst(callArgContext.literalExpr().scalarLiteral());
            else if (callArgContext.literalExpr() != null && callArgContext.literalExpr().stringLiteral() != null)
                rightSide = toAst(callArgContext.literalExpr().stringLiteral());
            else if (callArgContext.spacePathExpr() != null)
                rightSide = toAst(callArgContext.spacePathExpr());
            else if (callArgContext.actionCallExpr() != null)
                // nested
                rightSide = toAst(callArgContext.actionCallExpr());

            thisCallArgs[idxArg] = rightSide;

            idxArg++;
        }

        return astBuilder.newActionCallExpr("callPoint", spacePathExpr, thisCallArgs);
    }

    /** Transforms parse tree Space Path Expr to a nested list of nav operations. */
    private ActionCallExpr toAst(SpaceParser.SpacePathExprContext spacePathExprContext) {
        spacePathExprContext.spacePathExpr();
        spacePathExprContext.identifier();
        spacePathExprContext.SPathNavAssocToOper();
        ActionCallExpr nextCallExpr = toAst(spacePathExprContext.spacePathExpr());
        ActionCallExpr actionCallExpr =
            astBuilder.newActionCallExpr("callPoint",
                                         NAV_OPER,
                                         nextCallExpr
                                         );
            nextCallExpr.addChild(actionCallExpr);
        return actionCallExpr;
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

    private SpaceDefn toAst(List<SpaceParser.AnySpaceElementDefnContext> anySpaceElementDefnContexts) {
        log.info("transforming list of " + SpaceParser.AnySpaceElementDefnContext.class.getSimpleName());

        SpaceDefn spaceDefn = astBuilder.newSpaceDefn(null) ;
        addToAst(spaceDefn, anySpaceElementDefnContexts);
        return spaceDefn;
    }

    private VariableDefn toAst(SpaceParser.VariableDefnContext variableDefnContext) {
        log.info("transforming " + SpaceParser.VariableDefnContext.class.getSimpleName());

        return astBuilder.newVariableDefn(toText(variableDefnContext.identifier()),
                                            toAst(variableDefnContext.primitiveTypeName()));
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
