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
        spaceDefn = astBuilder.newSpaceDefn(toString(spaceDefnContext.identifier()));
        spaceDefnContext.accessModifier();
        spaceDefnContext.defnTypeModifier();
        spaceDefnContext.elementDefnHeader();
        if (spaceDefnContext.ExtendsKeyword() != null) {
            spaceDefnContext.identifierRefList();
        }
        List<Space2Parser.AnyCoordinateOrActionDefnContext> bodyElements
            = spaceDefnContext.spaceDefnBody().anyCoordinateOrActionDefn();
        for (Space2Parser.AnyCoordinateOrActionDefnContext bodyElement : bodyElements) {
            if (bodyElement.coordinateDefn() != null) {

            }
            else if (bodyElement.associationDefn() != null) {

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
        return spaceDefn;
    }

    private AbstractActionDefn toAst(Space2Parser.ActionDefnContext actionDefnContext) {
        log.info("transforming " + Space2Parser.ActionDefnContext.class.getSimpleName());

        SpaceActionDefn actionDefn
            = astBuilder.newSpaceActionDefn(toString(actionDefnContext.identifier()));
        actionDefn.setArgSpaceDefn(toAst(actionDefnContext.coordinateDefn()));
        actionDefnContext.coordinateDefn();
        actionDefnContext.actionCallDefn();
        actionDefnContext.identifier();
        actionDefnContext.typeRef();
        return actionDefn;
    }

    private SpaceDefn toAst(List<Space2Parser.CoordinateDefnContext> coordinateDefnContexts) {
        log.info("transforming list of " + Space2Parser.CoordinateDefnContext.class.getSimpleName());

        SpaceDefn spaceDefn = astBuilder.newSpaceDefn(null) ;
        for (Space2Parser.CoordinateDefnContext coordinateDefnContext : coordinateDefnContexts) {
//            if (isPrimitiveDefn(co))
            spaceDefn.addDimension(toAst(coordinateDefnContext));
        }
        return spaceDefn;
    }

    private CoordinateDefn toAst(Space2Parser.CoordinateDefnContext coordinateDefnContext) {
        log.info("transforming " + Space2Parser.CoordinateDefnContext.class.getSimpleName());

        return astBuilder.newCoordinateDefn(toString(coordinateDefnContext.identifier()),
                                            toAst(coordinateDefnContext.typeRef()));
    }

    private PrimitiveType toAst(Space2Parser.TypeRefContext typeRefContext) {
        log.info("transforming " + Space2Parser.TypeRefContext.class.getSimpleName()
        + "=" + typeRefContext.getText());

        return PrimitiveType.valueOf(typeRefContext.primitiveTypeName().getText());
    }

    private String[] toNsArray(List<Space2Parser.IdentifierContext> fqNsIds) {
        String[] nsArray = new String[fqNsIds.size()];
        for (int idxId = 0; idxId < fqNsIds.size(); idxId++) {
            nsArray[idxId] = toString(fqNsIds.get(idxId));
        }
        return nsArray;
    }

    private String toString(Space2Parser.IdentifierContext identifierContext) {
        return identifierContext.Identifier().getSymbol().getText();
    }
}
