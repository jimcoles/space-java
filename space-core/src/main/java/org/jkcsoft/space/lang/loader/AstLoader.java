/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.loader;

import org.jkcsoft.space.lang.ast.Schema;

import java.io.File;

/**
 * An AstLoader loads an AST corresponding to a file or files on the file system.
 * Other types of loaders might also evolve, e.g., load from binary.  Several
 * AstLoader's might be used in a given Space runtime instance.
 *
 * @author Jim Coles
 */
public interface AstLoader {

    /** A loader should provide a unique name useful for logging and debugging. */
    String getName();

    /** Loaders should return the root (directory) node */
    Schema load(File file) throws Exception;

}
