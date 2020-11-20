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
 * <p>At it's most basic, a {@link TypeDefn} is just a set of constraints
 * superimposed on a byte sequence.
 *
 * @author Jim Coles
 */
public interface TypeDefn extends ContextDatumDefn, Named {

    boolean isPrimitiveType();

    boolean isComplexType();

    boolean isView();

    boolean isSimpleType();

    boolean isSetType();

    boolean isSequenceType();

    boolean isStreamType();

    boolean isAssignableTo(TypeDefn argsType);

    boolean hasPrimaryKey();

    KeyDefnImpl getPrimaryKeyDefn();

    Set<KeyDefnImpl> getAlternateKeyDefns();

    SequenceTypeDefn getSequenceOfType();

    ProjectionDecl addProjectionDecl(ProjectionDecl projectionDecl);

    FunctionDefn addFunctionDefn(FunctionDefn functionDefn);

    List<ProjectionDecl> getProjectionDeclList();

    List<Statement> getInitializations();

    Comparator getTypeComparator();

    SetTypeDefn getSetOfType();
}
