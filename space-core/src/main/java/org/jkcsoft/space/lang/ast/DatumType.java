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
 * declarative notions: Primitives, Domains, and Space Type's.
 *
 * @author Jim Coles
 */
public interface DatumType extends Named {

    MetaType getMetaType();

    int getScalarDofs();

    SequenceTypeDefn getSequenceOfType();

    boolean isPrimitive();

    boolean isAssignableTo(DatumType argsType);
}
