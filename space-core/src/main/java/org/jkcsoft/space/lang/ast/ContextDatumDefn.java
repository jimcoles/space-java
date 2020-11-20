/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.List;

/**
 * @author Jim Coles
 */
public interface ContextDatumDefn extends ModelElement {
    //
    ContextDatumDefn addVariableDecl(VariableDecl variableDecl);

    ContextDatumDefn addAssociationDecl(AssociationDefn associationDecl);

    ContextDatumDefn addInitExpression(ExprStatement<AssignmentExpr> assignmentExpr);

    boolean hasDatums();

    Declaration getDatum(String name);

    int getScalarDofs();

    List<VariableDecl> getVariablesDeclList();

    List<Declaration> getDatumDeclList();
}
