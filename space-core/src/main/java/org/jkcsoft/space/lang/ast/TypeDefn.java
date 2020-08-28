/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Basically, here to lend a polymorphic notion to our various structural
 * declarative notions: Primitives, Domains, and Type's.
 *
 * <p>At it's most basic, a {@link TypeDefn} is just a byte sequence with some
 * constraints superimposed.
 *
 * @author Jim Coles
 */
public interface TypeDefn extends Named {

    boolean isPrimitiveType();

    boolean isComplexType();

    boolean isView();

    boolean isSimpleType();

    boolean isSetType();

    boolean isSequenceType();

    boolean isStreamType();

    boolean isAssignableTo(TypeDefn argsType);


    //
    boolean hasDatums();

    Declaration getDatum(String name);

    int getScalarDofs();

    boolean hasPrimaryKey();

    KeyDefnImpl getPrimaryKeyDefn();

    Set<KeyDefnImpl> getAlternateKeyDefns();

    SequenceTypeDefn getSequenceOfType();

    SetTypeDefn getSetOfType();

    VariableDecl addVariableDecl(VariableDecl variableDecl);

    AssociationDefn addAssociationDecl(AssociationDefn associationDecl);

    ProjectionDecl addProjectionDecl(ProjectionDecl projectionDecl);

    List<ProjectionDecl> getProjectionDeclList();

    List<VariableDecl> getVariablesDeclList();

    List<Declaration> getDatumDeclList();

    StatementBlock getInitBlock();

    FunctionDefn addFunctionDefn(FunctionDefn functionDefn);

    Comparator getTypeComparator();
}
