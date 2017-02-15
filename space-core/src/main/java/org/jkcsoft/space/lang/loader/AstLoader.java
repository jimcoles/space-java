/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.loader;

import org.jkcsoft.space.lang.ast.AstBuilder;

import java.io.File;

/**
 * @author Jim Coles
 */
public interface AstLoader {

    /** Loads an AstBuilder object. */
    AstBuilder load(File file) throws Exception;

}
