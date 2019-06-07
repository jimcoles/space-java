/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.ModelElement;

/**
 * Used by Linker to walk AST paths specific to a reference's scope. E.g., rules
 * for resolving new Tuple LHS var are different that those for general var
 * resolving. This interface avoids clunky case logic.
 *
 * @author Jim Coles
 */
public interface AstPathIterator {

//    boolean hasContext();

//    ModelElement getContext();

    StaticScope moveToNext();
//    StaticScope getCurrent();

}
