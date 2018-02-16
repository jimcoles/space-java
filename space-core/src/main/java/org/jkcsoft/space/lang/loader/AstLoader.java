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

import org.jkcsoft.space.lang.ast.AstFactory;

import java.io.File;

/**
 * @author Jim Coles
 */
public interface AstLoader {

    /** Loads an AstFactory object. */
    AstFactory load(File file) throws Exception;

}
