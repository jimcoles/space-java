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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jkcsoft.space.antlr.Space2Parser;
import org.jkcsoft.space.antlr.Space2ParserBaseListener;
import org.jkcsoft.space.lang.ast.AstBuilder;

/**
 * Implements the transform from the raw ANTLR parse tree to the Space AST.
 *
 * This guy gets an 'enter__' and 'exit__' callback for every Parser Rule
 * matched by the parser.
 *
 * @author Jim Coles
 */
public class Ra2AstParserListener extends Space2ParserBaseListener {

    private AstBuilder astBuilder = new AstBuilder();

    @Override
    public void enterSpaceDefn(Space2Parser.SpaceDefnContext ctx) {
        astBuilder.initProgram();
//        astBuilder.getAstRoot().addSpaceDefn(new EntityDefn(null, ctx.ge))
        super.enterSpaceDefn(ctx);
    }

    @Override
    public void exitSpaceDefn(Space2Parser.SpaceDefnContext ctx) {
        super.exitSpaceDefn(ctx);
    }

    @Override
    public void enterAccessModifier(Space2Parser.AccessModifierContext ctx) {
        super.enterAccessModifier(ctx);
    }

    @Override
    public void exitAccessModifier(Space2Parser.AccessModifierContext ctx) {
        super.exitAccessModifier(ctx);
    }

    @Override
    public void enterDefnTypeModifier(Space2Parser.DefnTypeModifierContext ctx) {
        super.enterDefnTypeModifier(ctx);
    }

    @Override
    public void exitDefnTypeModifier(Space2Parser.DefnTypeModifierContext ctx) {
        super.exitDefnTypeModifier(ctx);
    }

    @Override
    public void enterElementDefnHeader(Space2Parser.ElementDefnHeaderContext ctx) {
        super.enterElementDefnHeader(ctx);
    }

    @Override
    public void exitElementDefnHeader(Space2Parser.ElementDefnHeaderContext ctx) {
        super.exitElementDefnHeader(ctx);
    }

    @Override
    public void enterSpaceDefnBody(Space2Parser.SpaceDefnBodyContext ctx) {
        super.enterSpaceDefnBody(ctx);
    }

    @Override
    public void exitSpaceDefnBody(Space2Parser.SpaceDefnBodyContext ctx) {
        super.exitSpaceDefnBody(ctx);
    }

    @Override
    public void enterAnyCoordinateOrActionDefn(Space2Parser.AnyCoordinateOrActionDefnContext ctx) {
        super.enterAnyCoordinateOrActionDefn(ctx);
    }

    @Override
    public void exitAnyCoordinateOrActionDefn(Space2Parser.AnyCoordinateOrActionDefnContext ctx) {
        super.exitAnyCoordinateOrActionDefn(ctx);
    }

    @Override
    public void enterCoordinateDefn(Space2Parser.CoordinateDefnContext ctx) {
        super.enterCoordinateDefn(ctx);
    }

    @Override
    public void exitCoordinateDefn(Space2Parser.CoordinateDefnContext ctx) {
        super.exitCoordinateDefn(ctx);
    }

    @Override
    public void enterAssociationDefn(Space2Parser.AssociationDefnContext ctx) {
        super.enterAssociationDefn(ctx);
    }

    @Override
    public void exitAssociationDefn(Space2Parser.AssociationDefnContext ctx) {
        super.exitAssociationDefn(ctx);
    }

    @Override
    public void enterActionDefn(Space2Parser.ActionDefnContext ctx) {
        super.enterActionDefn(ctx);
    }

    @Override
    public void exitActionDefn(Space2Parser.ActionDefnContext ctx) {
        super.exitActionDefn(ctx);
    }

    @Override
    public void enterSetDecl(Space2Parser.SetDeclContext ctx) {
        super.enterSetDecl(ctx);
    }

    @Override
    public void exitSetDecl(Space2Parser.SetDeclContext ctx) {
        super.exitSetDecl(ctx);
    }

    @Override
    public void enterSpaceDecl(Space2Parser.SpaceDeclContext ctx) {
        super.enterSpaceDecl(ctx);
    }

    @Override
    public void exitSpaceDecl(Space2Parser.SpaceDeclContext ctx) {
        super.exitSpaceDecl(ctx);
    }

    @Override
    public void enterComment(Space2Parser.CommentContext ctx) {
        super.enterComment(ctx);
    }

    @Override
    public void exitComment(Space2Parser.CommentContext ctx) {
        super.exitComment(ctx);
    }

    @Override
    public void enterSingleLineComment(Space2Parser.SingleLineCommentContext ctx) {
        super.enterSingleLineComment(ctx);
    }

    @Override
    public void exitSingleLineComment(Space2Parser.SingleLineCommentContext ctx) {
        super.exitSingleLineComment(ctx);
    }

    @Override
    public void enterMultiLineComment(Space2Parser.MultiLineCommentContext ctx) {
        super.enterMultiLineComment(ctx);
    }

    @Override
    public void exitMultiLineComment(Space2Parser.MultiLineCommentContext ctx) {
        super.exitMultiLineComment(ctx);
    }

    @Override
    public void enterPrimitiveTypeName(Space2Parser.PrimitiveTypeNameContext ctx) {
        super.enterPrimitiveTypeName(ctx);
    }

    @Override
    public void exitPrimitiveTypeName(Space2Parser.PrimitiveTypeNameContext ctx) {
        super.exitPrimitiveTypeName(ctx);
    }

    @Override
    public void enterAssignment(Space2Parser.AssignmentContext ctx) {
        super.enterAssignment(ctx);
    }

    @Override
    public void exitAssignment(Space2Parser.AssignmentContext ctx) {
        super.exitAssignment(ctx);
    }

    @Override
    public void enterLiteral(Space2Parser.LiteralContext ctx) {
        super.enterLiteral(ctx);
    }

    @Override
    public void exitLiteral(Space2Parser.LiteralContext ctx) {
        super.exitLiteral(ctx);
    }

    @Override
    public void enterString(Space2Parser.StringContext ctx) {
        super.enterString(ctx);
    }

    @Override
    public void exitString(Space2Parser.StringContext ctx) {
        super.exitString(ctx);
    }

    @Override
    public void enterInteger(Space2Parser.IntegerContext ctx) {
        super.enterInteger(ctx);
    }

    @Override
    public void exitInteger(Space2Parser.IntegerContext ctx) {
        super.exitInteger(ctx);
    }

    @Override
    public void enterFloatLit(Space2Parser.FloatLitContext ctx) {
        super.enterFloatLit(ctx);
    }

    @Override
    public void exitFloatLit(Space2Parser.FloatLitContext ctx) {
        super.exitFloatLit(ctx);
    }

    @Override
    public void enterIdentifier(Space2Parser.IdentifierContext ctx) {
        super.enterIdentifier(ctx);
    }

    @Override
    public void exitIdentifier(Space2Parser.IdentifierContext ctx) {
        super.exitIdentifier(ctx);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        super.enterEveryRule(ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        ctx.getChild(0);
        super.exitEveryRule(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        node.getText();
        node.getSymbol().getText();
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
