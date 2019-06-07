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

/**
 * The Linker sets the scope kind of a resolved meta object (e.g., a DATUM)
 * to tell the Executor where to find the runtime instance-level object.
 *
 * @author Jim Coles
 */
public enum ScopeKind {
    BLOCK,
    ARG,
    SPACE_DEFN,
    REF_TO_TYPE,
    STATIC
}
