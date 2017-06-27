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
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.antlr.SpaceParserBaseListener;
import org.jkcsoft.space.lang.ast.AstBuilder;

/**
 * Implements the transform from the raw ANTLR parse tree to the Space AST.
 *
 * This guy gets an 'enter__' and 'exit__' callback for every Parser Rule
 * matched by the parser.
 *
 * @author Jim Coles
 */
public class Ra2AstParserListener extends SpaceParserBaseListener {

    private AstBuilder astBuilder = new AstBuilder();

    @Override
    public void enterSpaceDefn(SpaceParser.SpaceDefnContext ctx) {
        astBuilder.initProgram();
//        astBuilder.getAstRoot().addSpaceDefn(new EntityDefn(null, ctx.ge))
        super.enterSpaceDefn(ctx);
    }

    @Override
    public void exitSpaceDefn(SpaceParser.SpaceDefnContext ctx) {
        super.exitSpaceDefn(ctx);
    }

    @Override
    public void enterAccessModifier(SpaceParser.AccessModifierContext ctx) {
        super.enterAccessModifier(ctx);
    }

    @Override
    public void exitAccessModifier(SpaceParser.AccessModifierContext ctx) {
        super.exitAccessModifier(ctx);
    }

    @Override
    public void enterDefnTypeModifier(SpaceParser.DefnTypeModifierContext ctx) {
        super.enterDefnTypeModifier(ctx);
    }

    @Override
    public void exitDefnTypeModifier(SpaceParser.DefnTypeModifierContext ctx) {
        super.exitDefnTypeModifier(ctx);
    }

    @Override
    public void enterElementDefnHeader(SpaceParser.ElementDefnHeaderContext ctx) {
        super.enterElementDefnHeader(ctx);
    }

    @Override
    public void exitElementDefnHeader(SpaceParser.ElementDefnHeaderContext ctx) {
        super.exitElementDefnHeader(ctx);
    }

    @Override
    public void enterSpaceDefnBody(SpaceParser.SpaceDefnBodyContext ctx) {
        super.enterSpaceDefnBody(ctx);
    }

    @Override
    public void exitSpaceDefnBody(SpaceParser.SpaceDefnBodyContext ctx) {
        super.exitSpaceDefnBody(ctx);
    }

    @Override
    public void enterAssociationDefn(SpaceParser.AssociationDefnContext ctx) {
        super.enterAssociationDefn(ctx);
    }

    @Override
    public void exitAssociationDefn(SpaceParser.AssociationDefnContext ctx) {
        super.exitAssociationDefn(ctx);
    }

    @Override
    public void enterActionDefn(SpaceParser.ActionDefnContext ctx) {
        super.enterActionDefn(ctx);
    }

    @Override
    public void exitActionDefn(SpaceParser.ActionDefnContext ctx) {
        super.exitActionDefn(ctx);
    }


    @Override
    public void enterSpaceDecl(SpaceParser.SpaceDeclContext ctx) {
        super.enterSpaceDecl(ctx);
    }

    @Override
    public void exitSpaceDecl(SpaceParser.SpaceDeclContext ctx) {
        super.exitSpaceDecl(ctx);
    }

    @Override
    public void enterComment(SpaceParser.CommentContext ctx) {
        super.enterComment(ctx);
    }

    @Override
    public void exitComment(SpaceParser.CommentContext ctx) {
        super.exitComment(ctx);
    }

    @Override
    public void enterSingleLineComment(SpaceParser.SingleLineCommentContext ctx) {
        super.enterSingleLineComment(ctx);
    }

    @Override
    public void exitSingleLineComment(SpaceParser.SingleLineCommentContext ctx) {
        super.exitSingleLineComment(ctx);
    }

    @Override
    public void enterMultiLineComment(SpaceParser.MultiLineCommentContext ctx) {
        super.enterMultiLineComment(ctx);
    }

    @Override
    public void exitMultiLineComment(SpaceParser.MultiLineCommentContext ctx) {
        super.exitMultiLineComment(ctx);
    }

    @Override
    public void enterPrimitiveTypeName(SpaceParser.PrimitiveTypeNameContext ctx) {
        super.enterPrimitiveTypeName(ctx);
    }

    @Override
    public void exitPrimitiveTypeName(SpaceParser.PrimitiveTypeNameContext ctx) {
        super.exitPrimitiveTypeName(ctx);
    }

    @Override
    public void enterIdentifier(SpaceParser.IdentifierContext ctx) {
        super.enterIdentifier(ctx);
    }

    @Override
    public void exitIdentifier(SpaceParser.IdentifierContext ctx) {
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
