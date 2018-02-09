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

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.antlr.SpaceParserBaseVisitor;
import org.jkcsoft.space.antlr.SpaceParserVisitor;
import org.jkcsoft.space.lang.ast.AstBuilder;

/**
 * Each visitXyz( ) transforms from the raw ANTLR parse tree node to an AST node
 * and then adds the resulting AST node to the AST tree.
 */
public class Antrl2AstTransVisitor extends SpaceParserBaseVisitor<AstBuilder> {

    private AstBuilder astBuilder;

    public void init() {
        astBuilder = new AstBuilder();
        astBuilder.initProgram("");
    }

    @Override
    public AstBuilder visitParseUnit(SpaceParser.ParseUnitContext ctx) {
        return super.visitParseUnit(ctx);
    }

    @Override
    public AstBuilder visitParseUnitRelational(SpaceParser.ParseUnitRelationalContext ctx) {
        return super.visitParseUnitRelational(ctx);
    }

    @Override
    public AstBuilder visitAnyThing(SpaceParser.AnyThingContext ctx) {
        return super.visitAnyThing(ctx);
    }

    @Override
    public AstBuilder visitSpaceTypeDefn(SpaceParser.SpaceTypeDefnContext ctx) {
        return super.visitSpaceTypeDefn(ctx);
    }

    @Override
    public AstBuilder visitEquationDefn(SpaceParser.EquationDefnContext ctx) {
        return super.visitEquationDefn(ctx);
    }

    @Override
    public AstBuilder visitAccessModifier(SpaceParser.AccessModifierContext ctx) {
        return super.visitAccessModifier(ctx);
    }

    @Override
    public AstBuilder visitDefnTypeModifier(SpaceParser.DefnTypeModifierContext ctx) {
        return super.visitDefnTypeModifier(ctx);
    }

    @Override
    public AstBuilder visitElementDefnHeader(SpaceParser.ElementDefnHeaderContext ctx) {
        return super.visitElementDefnHeader(ctx);
    }

    @Override
    public AstBuilder visitSpaceTypeDefnBody(SpaceParser.SpaceTypeDefnBodyContext ctx) {
        return super.visitSpaceTypeDefnBody(ctx);
    }

    @Override
    public AstBuilder visitVariableDefnStmnt(SpaceParser.VariableDefnStmntContext ctx) {
        return super.visitVariableDefnStmnt(ctx);
    }

    @Override
    public AstBuilder visitVariableDefn(SpaceParser.VariableDefnContext ctx) {
        return super.visitVariableDefn(ctx);
    }

    @Override
    public AstBuilder visitVariableDecl(SpaceParser.VariableDeclContext ctx) {
        return super.visitVariableDecl(ctx);
    }

    @Override
    public AstBuilder visitAssociationDefnStmnt(SpaceParser.AssociationDefnStmntContext ctx) {
        return super.visitAssociationDefnStmnt(ctx);
    }

    @Override
    public AstBuilder visitAssociationDefn(SpaceParser.AssociationDefnContext ctx) {
        return super.visitAssociationDefn(ctx);
    }

    @Override
    public AstBuilder visitAssociationDecl(SpaceParser.AssociationDeclContext ctx) {
        return super.visitAssociationDecl(ctx);
    }

    @Override
    public AstBuilder visitParameterDecl(SpaceParser.ParameterDeclContext ctx) {
        return super.visitParameterDecl(ctx);
    }

    @Override
    public AstBuilder visitActionDefn(SpaceParser.ActionDefnContext ctx) {
        return super.visitActionDefn(ctx);
    }

    @Override
    public AstBuilder visitActionDefnBody(SpaceParser.ActionDefnBodyContext ctx) {
        return super.visitActionDefnBody(ctx);
    }

    @Override
    public AstBuilder visitStatement(SpaceParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public AstBuilder visitExpression(SpaceParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public AstBuilder visitActionCallExpr(SpaceParser.ActionCallExprContext ctx) {
        return super.visitActionCallExpr(ctx);
    }

    @Override
    public AstBuilder visitObjectExpr(SpaceParser.ObjectExprContext ctx) {
        return super.visitObjectExpr(ctx);
    }

    @Override
    public AstBuilder visitValueExpr(SpaceParser.ValueExprContext ctx) {
        return super.visitValueExpr(ctx);
    }

    @Override
    public AstBuilder visitValueOrAssignmentExprList(SpaceParser.ValueOrAssignmentExprListContext ctx) {
        return super.visitValueOrAssignmentExprList(ctx);
    }

    @Override
    public AstBuilder visitValueOrAssignmentExpr(SpaceParser.ValueOrAssignmentExprContext ctx) {
        return super.visitValueOrAssignmentExpr(ctx);
    }

    @Override
    public AstBuilder visitSetLiteral(SpaceParser.SetLiteralContext ctx) {
        return super.visitSetLiteral(ctx);
    }

    @Override
    public AstBuilder visitSpaceDecl(SpaceParser.SpaceDeclContext ctx) {
        return super.visitSpaceDecl(ctx);
    }

    @Override
    public AstBuilder visitComment(SpaceParser.CommentContext ctx) {
        return super.visitComment(ctx);
    }

    @Override
    public AstBuilder visitSingleLineComment(SpaceParser.SingleLineCommentContext ctx) {
        return super.visitSingleLineComment(ctx);
    }

    @Override
    public AstBuilder visitMultiLineComment(SpaceParser.MultiLineCommentContext ctx) {
        return super.visitMultiLineComment(ctx);
    }

    @Override
    public AstBuilder visitAnyTypeRef(SpaceParser.AnyTypeRefContext ctx) {
        return super.visitAnyTypeRef(ctx);
    }

    @Override
    public AstBuilder visitPrimitiveTypeName(SpaceParser.PrimitiveTypeNameContext ctx) {
        return super.visitPrimitiveTypeName(ctx);
    }

    @Override
    public AstBuilder visitRightAssignmentExpr(SpaceParser.RightAssignmentExprContext ctx) {
        return super.visitRightAssignmentExpr(ctx);
    }

    @Override
    public AstBuilder visitAssignmentExpr(SpaceParser.AssignmentExprContext ctx) {
        return super.visitAssignmentExpr(ctx);
    }

    @Override
    public AstBuilder visitLiteralExpr(SpaceParser.LiteralExprContext ctx) {
        return super.visitLiteralExpr(ctx);
    }

    @Override
    public AstBuilder visitScalarLiteral(SpaceParser.ScalarLiteralContext ctx) {
        return super.visitScalarLiteral(ctx);
    }

    @Override
    public AstBuilder visitStringLiteral(SpaceParser.StringLiteralContext ctx) {
        return super.visitStringLiteral(ctx);
    }

    @Override
    public AstBuilder visitIntegerLiteral(SpaceParser.IntegerLiteralContext ctx) {
        return super.visitIntegerLiteral(ctx);
    }

    @Override
    public AstBuilder visitFloatLiteral(SpaceParser.FloatLiteralContext ctx) {
        return super.visitFloatLiteral(ctx);
    }

    @Override
    public AstBuilder visitIdentifier(SpaceParser.IdentifierContext ctx) {
        return super.visitIdentifier(ctx);
    }

    @Override
    public AstBuilder visitQueryDefn(SpaceParser.QueryDefnContext ctx) {
        return super.visitQueryDefn(ctx);
    }

    @Override
    public AstBuilder visitSpacePathExpr(SpaceParser.SpacePathExprContext ctx) {
        return super.visitSpacePathExpr(ctx);
    }

    @Override
    public AstBuilder visitSpacePathList(SpaceParser.SpacePathListContext ctx) {
        return super.visitSpacePathList(ctx);
    }

    @Override
    public AstBuilder visitGrammarExpression(SpaceParser.GrammarExpressionContext ctx) {
        return super.visitGrammarExpression(ctx);
    }

    @Override
    public AstBuilder visitRegularExpr(SpaceParser.RegularExprContext ctx) {
        return super.visitRegularExpr(ctx);
    }
}
