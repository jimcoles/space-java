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
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.antlr.SpaceParserBaseListener;
import org.jkcsoft.space.lang.ast.AstFactory;

import java.io.File;

/**
 * Implements the transform from the raw ANTLR parse tree to the Space AST.
 *
 * This guy gets an 'enter__' and 'exit__' callback for every Parser Rule
 * matched by the parser.
 *
 * @author Jim Coles
 */
public class Antlr2AstParserListener extends SpaceParserBaseListener {

    private AstFactory astFactory = new AstFactory();
    private File file;
    private int parseRuleCount;
    private int parseTerminalCount;

    public Antlr2AstParserListener(File file) {
        this.file = file;
    }

    @Override
    public void enterSpaceTypeDefn(SpaceParser.SpaceTypeDefnContext ctx) {
        super.enterSpaceTypeDefn(ctx);
    }

    @Override
    public void exitSpaceTypeDefn(SpaceParser.SpaceTypeDefnContext ctx) {
        super.exitSpaceTypeDefn(ctx);
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
    public void enterSpaceTypeDefnBody(SpaceParser.SpaceTypeDefnBodyContext ctx) {
        super.enterSpaceTypeDefnBody(ctx);
    }

    @Override
    public void exitSpaceTypeDefnBody(SpaceParser.SpaceTypeDefnBodyContext ctx) {
        super.exitSpaceTypeDefnBody(ctx);
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
    public void enterFunctionDefn(SpaceParser.FunctionDefnContext ctx) {
        super.enterFunctionDefn(ctx);
    }

    @Override
    public void exitFunctionDefn(SpaceParser.FunctionDefnContext ctx) {
        super.exitFunctionDefn(ctx);
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
        this.parseRuleCount++;
        super.exitEveryRule(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        this.parseTerminalCount++;
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}
