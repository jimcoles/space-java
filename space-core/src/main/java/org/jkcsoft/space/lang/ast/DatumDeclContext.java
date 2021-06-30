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
public interface DatumDeclContext extends ModelElement {
    //
    DatumDeclContext addVariableDecl(VariableDecl variableDecl);

    DatumDeclContext addInitExpression(ExprStatement<AssignmentExpr> assignmentExpr);

    boolean hasDatums();

    DatumDecl getDatum(String name);

    int getScalarDofs();

    List<VariableDecl> getVariablesDeclList();

    List<DatumDecl> getDatumDeclList();
}
