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

import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.antlr.SpaceParserBaseVisitor;
import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.CodeSourceInfo;

/**
 * Each visitXyz( ) transforms from the raw ANTLR parse tree node to an AST node
 * and then adds the resulting AST node to the AST tree.
 */
public class Antrl2AstTransVisitor extends SpaceParserBaseVisitor<AstFactory> {

    private AstFactory astFactory;

    public void init() {
        astFactory = new AstFactory();
        astFactory.newProgram(new CodeSourceInfo(), "");
    }

    @Override
    public AstFactory visitParseUnit(SpaceParser.ParseUnitContext ctx) {
        return super.visitParseUnit(ctx);
    }

    @Override
    public AstFactory visitParseUnitRelational(SpaceParser.ParseUnitRelationalContext ctx) {
        return super.visitParseUnitRelational(ctx);
    }

    @Override
    public AstFactory visitAnyThing(SpaceParser.AnyThingContext ctx) {
        return super.visitAnyThing(ctx);
    }

    @Override
    public AstFactory visitSpaceTypeDefn(SpaceParser.SpaceTypeDefnContext ctx) {
        return super.visitSpaceTypeDefn(ctx);
    }

    @Override
    public AstFactory visitEquationDefn(SpaceParser.EquationDefnContext ctx) {
        return super.visitEquationDefn(ctx);
    }

    @Override
    public AstFactory visitAccessModifier(SpaceParser.AccessModifierContext ctx) {
        return super.visitAccessModifier(ctx);
    }

    @Override
    public AstFactory visitDefnTypeModifier(SpaceParser.DefnTypeModifierContext ctx) {
        return super.visitDefnTypeModifier(ctx);
    }

    @Override
    public AstFactory visitElementDefnHeader(SpaceParser.ElementDefnHeaderContext ctx) {
        return super.visitElementDefnHeader(ctx);
    }

    @Override
    public AstFactory visitSpaceTypeDefnBody(SpaceParser.SpaceTypeDefnBodyContext ctx) {
        return super.visitSpaceTypeDefnBody(ctx);
    }

    @Override
    public AstFactory visitVariableDefnStmt(SpaceParser.VariableDefnStmtContext ctx) {
        return super.visitVariableDefnStmt(ctx);
    }

    @Override
    public AstFactory visitVariableDefn(SpaceParser.VariableDefnContext ctx) {
        return super.visitVariableDefn(ctx);
    }

    @Override
    public AstFactory visitVariableDecl(SpaceParser.VariableDeclContext ctx) {
        return super.visitVariableDecl(ctx);
    }

    @Override
    public AstFactory visitAssociationDefnStmt(SpaceParser.AssociationDefnStmtContext ctx) {
        return super.visitAssociationDefnStmt(ctx);
    }

    @Override
    public AstFactory visitAssociationDefn(SpaceParser.AssociationDefnContext ctx) {
        return super.visitAssociationDefn(ctx);
    }

    @Override
    public AstFactory visitAssociationDecl(SpaceParser.AssociationDeclContext ctx) {
        return super.visitAssociationDecl(ctx);
    }

    @Override
    public AstFactory visitParameterDecl(SpaceParser.ParameterDeclContext ctx) {
        return super.visitParameterDecl(ctx);
    }

    @Override
    public AstFactory visitActionDefn(SpaceParser.ActionDefnContext ctx) {
        return super.visitActionDefn(ctx);
    }

    @Override
    public AstFactory visitActionDefnBody(SpaceParser.ActionDefnBodyContext ctx) {
        return super.visitActionDefnBody(ctx);
    }

    @Override
    public AstFactory visitStatement(SpaceParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public AstFactory visitExpression(SpaceParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public AstFactory visitActionCallExpr(SpaceParser.ActionCallExprContext ctx) {
        return super.visitActionCallExpr(ctx);
    }

    @Override
    public AstFactory visitObjectExpr(SpaceParser.ObjectExprContext ctx) {
        return super.visitObjectExpr(ctx);
    }

    @Override
    public AstFactory visitValueExpr(SpaceParser.ValueExprContext ctx) {
        return super.visitValueExpr(ctx);
    }

    @Override
    public AstFactory visitValueOrAssignmentExprList(SpaceParser.ValueOrAssignmentExprListContext ctx) {
        return super.visitValueOrAssignmentExprList(ctx);
    }

    @Override
    public AstFactory visitValueOrAssignmentExpr(SpaceParser.ValueOrAssignmentExprContext ctx) {
        return super.visitValueOrAssignmentExpr(ctx);
    }

    @Override
    public AstFactory visitSetLiteral(SpaceParser.SetLiteralContext ctx) {
        return super.visitSetLiteral(ctx);
    }

    @Override
    public AstFactory visitSpaceDecl(SpaceParser.SpaceDeclContext ctx) {
        return super.visitSpaceDecl(ctx);
    }

    @Override
    public AstFactory visitComment(SpaceParser.CommentContext ctx) {
        return super.visitComment(ctx);
    }

    @Override
    public AstFactory visitSingleLineComment(SpaceParser.SingleLineCommentContext ctx) {
        return super.visitSingleLineComment(ctx);
    }

    @Override
    public AstFactory visitMultiLineComment(SpaceParser.MultiLineCommentContext ctx) {
        return super.visitMultiLineComment(ctx);
    }

    @Override
    public AstFactory visitAnyTypeRef(SpaceParser.AnyTypeRefContext ctx) {
        return super.visitAnyTypeRef(ctx);
    }

    @Override
    public AstFactory visitPrimitiveTypeName(SpaceParser.PrimitiveTypeNameContext ctx) {
        return super.visitPrimitiveTypeName(ctx);
    }

    @Override
    public AstFactory visitRightAssignmentExpr(SpaceParser.RightAssignmentExprContext ctx) {
        return super.visitRightAssignmentExpr(ctx);
    }

    @Override
    public AstFactory visitAssignmentExpr(SpaceParser.AssignmentExprContext ctx) {
        return super.visitAssignmentExpr(ctx);
    }

    @Override
    public AstFactory visitLiteralExpr(SpaceParser.LiteralExprContext ctx) {
        return super.visitLiteralExpr(ctx);
    }

    @Override
    public AstFactory visitScalarLiteral(SpaceParser.ScalarLiteralContext ctx) {
        return super.visitScalarLiteral(ctx);
    }

    @Override
    public AstFactory visitStringLiteral(SpaceParser.StringLiteralContext ctx) {
        return super.visitStringLiteral(ctx);
    }

    @Override
    public AstFactory visitIntegerLiteral(SpaceParser.IntegerLiteralContext ctx) {
        return super.visitIntegerLiteral(ctx);
    }

    @Override
    public AstFactory visitFloatLiteral(SpaceParser.FloatLiteralContext ctx) {
        return super.visitFloatLiteral(ctx);
    }

    @Override
    public AstFactory visitIdentifier(SpaceParser.IdentifierContext ctx) {
        return super.visitIdentifier(ctx);
    }

    @Override
    public AstFactory visitQueryDefn(SpaceParser.QueryDefnContext ctx) {
        return super.visitQueryDefn(ctx);
    }

    @Override
    public AstFactory visitSpacePathExpr(SpaceParser.SpacePathExprContext ctx) {
        return super.visitSpacePathExpr(ctx);
    }

    @Override
    public AstFactory visitSpacePathList(SpaceParser.SpacePathListContext ctx) {
        return super.visitSpacePathList(ctx);
    }

    @Override
    public AstFactory visitGrammarExpression(SpaceParser.GrammarExpressionContext ctx) {
        return super.visitGrammarExpression(ctx);
    }

    @Override
    public AstFactory visitRegularExpr(SpaceParser.RegularExprContext ctx) {
        return super.visitRegularExpr(ctx);
    }
}
