/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declartion;

/**
 * Variables and References hold values.
 *
 * @author Jim Coles
 */
public interface ValueHolder<V extends Value> {

    DatumType getType();

    V getValue();

    Declartion getDeclaration();
}
