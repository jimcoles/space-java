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
import org.jkcsoft.space.antlr.Space2Parser;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.runtime.ExecState;
import org.jkcsoft.space.util.Namespace;

import java.lang.reflect.Method;
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

    private ExecState state = ExecState.INITIALIZATION;
    private List<AstLoadError> errors = new LinkedList();
    private AstBuilder astBuilder;
//    private ObjectBuilder objBuilder = ObjectBuilder.getInstance();

    public AstBuilder toAst(Space2Parser.ParseUnitContext spaceParseUnit) {
        log.info("transforming " + Space2Parser.ParseUnitContext.class.getSimpleName());

        astBuilder = new AstBuilder();
        astBuilder.initProgram();
        astBuilder.getAstRoot().addSpaceDefn(toAst(spaceParseUnit.spaceDefn()));
        return astBuilder;
    }

    private SpaceDefn toAst(Space2Parser.SpaceDefnContext spaceDefnContext) {
        log.info("transforming " + Space2Parser.SpaceDefnContext.class.getSimpleName());

        SpaceDefn spaceDefn = null;
        // TODO: 2/8/17 Not all spaces are Entities?
        spaceDefn = astBuilder.newSpaceDefn(toText(spaceDefnContext.identifier()));
        spaceDefnContext.accessModifier();
        spaceDefnContext.defnTypeModifier();
        spaceDefnContext.elementDefnHeader();
        if (spaceDefnContext.ExtendsKeyword() != null) {
            spaceDefnContext.identifierRefList();
        }
        List<Space2Parser.AnySpaceElementDefnContext> bodyElements
            = spaceDefnContext.spaceDefnBody().anySpaceElementDefn();
        addToAst(spaceDefn, bodyElements);
        return spaceDefn;
    }

    private void addToAst(SpaceDefn spaceDefn, List<Space2Parser.AnySpaceElementDefnContext> bodyElements) {
        for (Space2Parser.AnySpaceElementDefnContext bodyElement : bodyElements) {
            if (bodyElement.coordinateDefn() != null) {
                spaceDefn.addDimension(toAst(bodyElement.coordinateDefn()));
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

    private AbstractActionDefn toAst(Space2Parser.ActionDefnContext actionDefnContext) {
        log.info("transforming " + Space2Parser.ActionDefnContext.class.getSimpleName());

        SpaceActionDefn actionDefn
            = astBuilder.newSpaceActionDefn(toText(actionDefnContext.identifier()));
        actionDefn.setArgSpaceDefn(toAst(actionDefnContext.anySpaceElementDefn()));
        List<Space2Parser.ActionCallDefnContext> actionCallDefnContexts
            = actionDefnContext.actionDefnBody().actionCallDefn();
        for (Space2Parser.ActionCallDefnContext actionCallDefnContext : actionCallDefnContexts) {
            actionDefn.addAction(toAst(actionCallDefnContext));
        }
        actionDefnContext.identifier();
        actionDefnContext.anyTypeRef();
        return actionDefn;
    }

    private CallActionDefn toAst(Space2Parser.ActionCallDefnContext actionCallDefnContext) {
        // TODO: 2/15/17 Resolve FQN of called function against imports.
        Namespace ns = new Namespace(toNsArray(collapseList(actionCallDefnContext.identifierRef())));

        String fragileFuncName = ns.getAbsolutePath();

        List<Space2Parser.ValueExprContext> callArgContexts = actionCallDefnContext.valueExpr();
        AssignmentDefn[] thisCallArgs = new AssignmentDefn[callArgContexts.size()];
        int idxArg = 0;
        for (Space2Parser.ValueExprContext callArgContext : callArgContexts) {
            AssignableDefn rightSide = null;
            if (callArgContext.literal() != null &  callArgContext.literal().scalarLiteral() != null)
                rightSide = toAst(callArgContext.literal().scalarLiteral());
            else if (callArgContext.literal() != null && callArgContext.literal().stringLiteral() != null)
                rightSide = toAst(callArgContext.literal().stringLiteral());
            else if (callArgContext.identifierRef() != null)
                rightSide = toAst(callArgContext.identifierRef());
            else if (callArgContext.actionCallDefn() != null)
                // nested
                rightSide = toAst(callArgContext.actionCallDefn());

            thisCallArgs[idxArg] = astBuilder.newAssignmentDefn(null, rightSide);

            idxArg++;
        }

        return astBuilder.newCallActionDefn(fragileFuncName, thisCallArgs);
    }

    private AssignableDefn toAst(Space2Parser.IdentifierRefContext identifierRefContext) {
        return astBuilder.newIdentifierRefDefn(toNsArray(collapseList(identifierRefContext)));
    }

    /**
     * Character string literals (CharactersSequences) produce, in effect, a 'new CharacterSequence("")'
     * call. */
    private AssignableDefn toAst(Space2Parser.StringLiteralContext stringLiteralContext) {
        AssignableDefn rightSide = null;

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

    private AssignableDefn toAst(Space2Parser.ScalarLiteralContext scalarLiteralContext) {
        return astBuilder.newLiteralHolder(scalarLiteralContext.integerLiteral().getText());
    }

    private List<Space2Parser.IdentifierContext> collapseList(Space2Parser.IdentifierRefContext identifierRefContext) {
        List<Space2Parser.IdentifierContext> listOfIds = new LinkedList<>();
        listOfIds.addAll(identifierRefContext.identifier());
        return listOfIds;
    }

    private SpaceDefn toAst(List<Space2Parser.AnySpaceElementDefnContext> coordinateDefnContexts) {
        log.info("transforming list of " + Space2Parser.AnySpaceElementDefnContext.class.getSimpleName());

        SpaceDefn spaceDefn = astBuilder.newSpaceDefn(null) ;
        addToAst(spaceDefn, coordinateDefnContexts);
        return spaceDefn;
    }

    private CoordinateDefn toAst(Space2Parser.CoordinateDefnContext coordinateDefnContext) {
        log.info("transforming " + Space2Parser.CoordinateDefnContext.class.getSimpleName());

        return astBuilder.newCoordinateDefn(toText(coordinateDefnContext.identifier()),
                                            toAst(coordinateDefnContext.primitiveTypeName()));
    }

    private PrimitiveType toAst(Space2Parser.PrimitiveTypeNameContext primitiveTypeNameContext) {
        log.info("transforming " + Space2Parser.PrimitiveTypeNameContext.class.getSimpleName()
        + "=" + primitiveTypeNameContext.getText());

        return PrimitiveType.valueOf(primitiveTypeNameContext.getText().toUpperCase());
    }

    private String[] toNsArray(List<Space2Parser.IdentifierContext> fqNsIds) {
        String[] nsArray = new String[fqNsIds.size()];
        for (int idxId = 0; idxId < fqNsIds.size(); idxId++) {
            nsArray[idxId] = toText(fqNsIds.get(idxId));
        }
        return nsArray;
    }

    private String toText(Space2Parser.IdentifierContext identifierContext) {
        return identifierContext.Identifier().getSymbol().getText();
    }
}
