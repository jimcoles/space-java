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
import org.antlr.v4.runtime.tree.RuleNode;
import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.antlr.SpaceParserBaseVisitor;
import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.ModelElement;
import org.jkcsoft.space.lang.ast.NamePart;
import org.jkcsoft.space.lang.ast.SourceInfo;

import java.io.File;

/**
 * Each visitXyz( ) transforms from the raw ANTLR parse tree node to an AST node
 * and then adds the resulting AST node to the AST tree.
 */
public class Antrl2AstTransVisitor extends SpaceParserBaseVisitor<ModelElement> {

    private AstFactory astFactory;
    private ModelElement currentNode;
    private File srcFile;

    public Antrl2AstTransVisitor(File srcFile) {
        super();
        this.srcFile = srcFile;
    }

    public void init() {
        astFactory = new AstFactory();
    }

    @Override
    public ModelElement visitChildren(RuleNode node) {
        super.visitChildren(node);
        currentNode = currentNode.getParent();
        return currentNode;
    }

    @Override
    protected ModelElement aggregateResult(ModelElement aggregate, ModelElement nextResult) {
//        aggregate.addChild(nextResult);
        return aggregate;
    }

    @Override
    protected ModelElement defaultResult() {
        return currentNode;
    }

    @Override
    public ModelElement visitAnnotation(SpaceParser.AnnotationContext ctx) {
        return super.visitAnnotation(ctx);
    }

    @Override
    public ModelElement visitPackageStatement(SpaceParser.PackageStatementContext ctx) {
        return super.visitPackageStatement(ctx);
    }

    @Override
    public ModelElement visitImportStatement(SpaceParser.ImportStatementContext ctx) {
        return super.visitImportStatement(ctx);
    }

    @Override
    public ModelElement visitElementDeclHeader(SpaceParser.ElementDeclHeaderContext ctx) {
        return super.visitElementDeclHeader(ctx);
    }

    @Override
    public ModelElement visitDatumDefnStmt(SpaceParser.DatumDefnStmtContext ctx) {
        return super.visitDatumDefnStmt(ctx);
    }

    @Override
    public ModelElement visitFunctionDefn(SpaceParser.FunctionDefnContext ctx) {
        return super.visitFunctionDefn(ctx);
    }

    @Override
    public ModelElement visitIfStatement(SpaceParser.IfStatementContext ctx) {
        return super.visitIfStatement(ctx);
    }

    @Override
    public ModelElement visitForEachStatement(SpaceParser.ForEachStatementContext ctx) {
        return super.visitForEachStatement(ctx);
    }

    @Override
    public ModelElement visitReturnStatement(SpaceParser.ReturnStatementContext ctx) {
        return super.visitReturnStatement(ctx);
    }

    @Override
    public ModelElement visitFunctionCallExpr(SpaceParser.FunctionCallExprContext ctx) {
        return super.visitFunctionCallExpr(ctx);
    }

    @Override
    public ModelElement visitUnaryOperExpr(SpaceParser.UnaryOperExprContext ctx) {
        return super.visitUnaryOperExpr(ctx);
    }

    @Override
    public ModelElement visitUnaryOper(SpaceParser.UnaryOperContext ctx) {
        return super.visitUnaryOper(ctx);
    }

    @Override
    public ModelElement visitBinaryOperExpr(SpaceParser.BinaryOperExprContext ctx) {
        return super.visitBinaryOperExpr(ctx);
    }

    @Override
    public ModelElement visitBinaryOper(SpaceParser.BinaryOperContext ctx) {
        return super.visitBinaryOper(ctx);
    }

    @Override
    public ModelElement visitComplexTypeRef(SpaceParser.ComplexTypeRefContext ctx) {
        return super.visitComplexTypeRef(ctx);
    }

    @Override
    public ModelElement visitAnyCollectionMarker(SpaceParser.AnyCollectionMarkerContext ctx) {
        return super.visitAnyCollectionMarker(ctx);
    }

    @Override
    public ModelElement visitSetMarker(SpaceParser.SetMarkerContext ctx) {
        return super.visitSetMarker(ctx);
    }

    @Override
    public ModelElement visitSequenceMarker(SpaceParser.SequenceMarkerContext ctx) {
        return super.visitSequenceMarker(ctx);
    }

    @Override
    public ModelElement visitVoidTypeName(SpaceParser.VoidTypeNameContext ctx) {
        return super.visitVoidTypeName(ctx);
    }

    @Override
    public ModelElement visitUntypedTupleLiteral(SpaceParser.UntypedTupleLiteralContext ctx) {
        return super.visitUntypedTupleLiteral(ctx);
    }

    @Override
    public ModelElement visitNewSetExpr(SpaceParser.NewSetExprContext ctx) {
        return super.visitNewSetExpr(ctx);
    }

    @Override
    public ModelElement visitNewSequenceExpr(SpaceParser.NewSequenceExprContext ctx) {
        return super.visitNewSequenceExpr(ctx);
    }

    @Override
    public ModelElement visitLanguageKey(SpaceParser.LanguageKeyContext ctx) {
        return super.visitLanguageKey(ctx);
    }

    @Override
    public ModelElement visitSpacePathRootExpr(SpaceParser.SpacePathRootExprContext ctx) {
        return super.visitSpacePathRootExpr(ctx);
    }

    @Override
    public ModelElement visitSpacePathAnyNavOper(SpaceParser.SpacePathAnyNavOperContext ctx) {
        return super.visitSpacePathAnyNavOper(ctx);
    }

    @Override
    public ModelElement visitParseUnit(SpaceParser.ParseUnitContext ctx) {
        currentNode = astFactory.newProgram(null, "visitor program");
        return super.visitParseUnit(ctx);
    }

    @Override
    public ModelElement visitParseUnitRelational(SpaceParser.ParseUnitRelationalContext ctx) {
        currentNode = astFactory.newProgram(null, "visitor program");
        return super.visitParseUnitRelational(ctx);
    }

    @Override
    public ModelElement visitAnyThing(SpaceParser.AnyThingContext ctx) {
        return super.visitAnyThing(ctx);
    }

    @Override
    public ModelElement visitSpaceTypeDefn(SpaceParser.SpaceTypeDefnContext ctx) {
        currentNode = astFactory.newSpaceTypeDefn(getSourceInfo(ctx), ((NamePart) ctx.identifier().accept(this)));
        return super.visitSpaceTypeDefn(ctx);
    }

    @Override
    public ModelElement visitEquationDefn(SpaceParser.EquationDefnContext ctx) {
        return super.visitEquationDefn(ctx);
    }

    @Override
    public ModelElement visitAccessModifier(SpaceParser.AccessModifierContext ctx) {
        return super.visitAccessModifier(ctx);
    }

    @Override
    public ModelElement visitDefnTypeModifier(SpaceParser.DefnTypeModifierContext ctx) {
        return super.visitDefnTypeModifier(ctx);
    }

    @Override
    public ModelElement visitSpaceTypeDefnBody(SpaceParser.SpaceTypeDefnBodyContext ctx) {
        return super.visitSpaceTypeDefnBody(ctx);
    }

    @Override
    public ModelElement visitVariableDefnStmt(SpaceParser.VariableDefnStmtContext ctx) {
        return super.visitVariableDefnStmt(ctx);
    }

    @Override
    public ModelElement visitVariableDefn(SpaceParser.VariableDefnContext ctx) {
        return super.visitVariableDefn(ctx);
    }

    @Override
    public ModelElement visitVariableDecl(SpaceParser.VariableDeclContext ctx) {
        return super.visitVariableDecl(ctx);
    }

    @Override
    public ModelElement visitAssociationDefnStmt(SpaceParser.AssociationDefnStmtContext ctx) {
        return super.visitAssociationDefnStmt(ctx);
    }

    @Override
    public ModelElement visitAssociationDefn(SpaceParser.AssociationDefnContext ctx) {
        return super.visitAssociationDefn(ctx);
    }

    @Override
    public ModelElement visitAssociationDecl(SpaceParser.AssociationDeclContext ctx) {
        return super.visitAssociationDecl(ctx);
    }

    @Override
    public ModelElement visitParameterDecl(SpaceParser.ParameterDeclContext ctx) {
        return super.visitParameterDecl(ctx);
    }

    @Override
    public ModelElement visitParameterDefnList(SpaceParser.ParameterDefnListContext ctx) {
        return super.visitParameterDefnList(ctx);
    }

    @Override
    public ModelElement visitStatementBlock(SpaceParser.StatementBlockContext ctx) {
        return super.visitStatementBlock(ctx);
    }

    @Override
    public ModelElement visitBooleanLiteral(SpaceParser.BooleanLiteralContext ctx) {
        return super.visitBooleanLiteral(ctx);
    }

    @Override
    public ModelElement visitStatement(SpaceParser.StatementContext ctx) {
        return super.visitStatement(ctx);
    }

    @Override
    public ModelElement visitExpression(SpaceParser.ExpressionContext ctx) {
        return super.visitExpression(ctx);
    }

    @Override
    public ModelElement visitNewObjectExpr(SpaceParser.NewObjectExprContext ctx) {
        return super.visitNewObjectExpr(ctx);
    }

    @Override
    public ModelElement visitValueExprChain(SpaceParser.ValueExprChainContext ctx) {
        return super.visitValueExprChain(ctx);
    }

    @Override
    public ModelElement visitValueOrAssignmentExprList(SpaceParser.ValueOrAssignmentExprListContext ctx) {
        return super.visitValueOrAssignmentExprList(ctx);
    }

    @Override
    public ModelElement visitValueOrAssignmentExpr(SpaceParser.ValueOrAssignmentExprContext ctx) {
        return super.visitValueOrAssignmentExpr(ctx);
    }

    @Override
    public ModelElement visitComment(SpaceParser.CommentContext ctx) {
        return super.visitComment(ctx);
    }

    @Override
    public ModelElement visitSingleLineComment(SpaceParser.SingleLineCommentContext ctx) {
        return super.visitSingleLineComment(ctx);
    }

    @Override
    public ModelElement visitMultiLineComment(SpaceParser.MultiLineCommentContext ctx) {
        return super.visitMultiLineComment(ctx);
    }

    @Override
    public ModelElement visitAnyTypeRef(SpaceParser.AnyTypeRefContext ctx) {
        return super.visitAnyTypeRef(ctx);
    }

    @Override
    public ModelElement visitPrimitiveTypeName(SpaceParser.PrimitiveTypeNameContext ctx) {
        return super.visitPrimitiveTypeName(ctx);
    }

    @Override
    public ModelElement visitRightAssignmentExpr(SpaceParser.RightAssignmentExprContext ctx) {
        return super.visitRightAssignmentExpr(ctx);
    }

    @Override
    public ModelElement visitAssignmentExpr(SpaceParser.AssignmentExprContext ctx) {
        return super.visitAssignmentExpr(ctx);
    }

    @Override
    public ModelElement visitLiteralExpr(SpaceParser.LiteralExprContext ctx) {
        return super.visitLiteralExpr(ctx);
    }

    @Override
    public ModelElement visitScalarLiteral(SpaceParser.ScalarLiteralContext ctx) {
        return super.visitScalarLiteral(ctx);
    }

    @Override
    public ModelElement visitStringLiteral(SpaceParser.StringLiteralContext ctx) {
        return super.visitStringLiteral(ctx);
    }

    @Override
    public ModelElement visitIntegerLiteral(SpaceParser.IntegerLiteralContext ctx) {
        return super.visitIntegerLiteral(ctx);
    }

    @Override
    public ModelElement visitFloatLiteral(SpaceParser.FloatLiteralContext ctx) {
        return super.visitFloatLiteral(ctx);
    }

    @Override
    public ModelElement visitIdentifier(SpaceParser.IdentifierContext ctx) {
        currentNode = astFactory.newNamePart(getSourceInfo(ctx), ctx.getText());
        return super.visitIdentifier(ctx);
    }

    @Override
    public ModelElement visitQueryDefn(SpaceParser.QueryDefnContext ctx) {
        return super.visitQueryDefn(ctx);
    }


    @Override
    public ModelElement visitSpacePathList(SpaceParser.SpacePathListContext ctx) {
        return super.visitSpacePathList(ctx);
    }

    @Override
    public ModelElement visitGrammarExpression(SpaceParser.GrammarExpressionContext ctx) {
        return super.visitGrammarExpression(ctx);
    }

    @Override
    public ModelElement visitRegularExpr(SpaceParser.RegularExprContext ctx) {
        return super.visitRegularExpr(ctx);
    }

    private SourceInfo getSourceInfo(ParserRuleContext ctx) {
        return Antrl2AstMapping.toAst(srcFile, ctx);
    }
}
