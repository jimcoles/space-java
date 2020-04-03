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

import org.jkcsoft.space.lang.instance.ValueHolder;
import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * Basically, here to lend a polymorphic notion to our various structural
 * declarative notions: Primitives, Domains, and Type's.
 *
 * <p>At it's most basic, a {@link DatumType} is just a byte sequence with some
 * constraints superimposed.
 *
 * @author Jim Coles
 */
public interface DatumType extends Named {

    int getScalarDofs();

    boolean isPrimitiveType();

    boolean isSimpleType();

    boolean isComplexType();

    boolean isSetType();

    boolean isSequenceType();

    boolean isStreamType();

    boolean isAssignableTo(DatumType argsType);

    SequenceTypeDefn getSequenceOfType();

    SetTypeDefn getSetOfType();
}
