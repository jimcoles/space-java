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
 * @author Jim Coles
 */
public interface FunctionDefn extends Named {

    /** If true, indicates that the function is not evaluated by the Space executor but rather
     * by some external subsystem, usual the Java VM. */
    boolean isOpaque();

    TypeDefn getArgumentsDefn();

    boolean isReturnVoid();

    TypeDefn getReturnType();

    DatumDecl getReturnAnonDecl();

}
