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
 * The AST element is not defined in source code, but rather
 * is created programmatically as part of the language runtime
 * bootup.
 *
 * @author Jim Coles
 */
public class IntrinsicSourceInfo implements SourceInfo {

    @Override
    public FileCoord getStart() {
        return null;
    }

    @Override
    public FileCoord getStop() {
        return null;
    }

    @Override
    public String toString() {
        return "(intrin)";
    }
}
