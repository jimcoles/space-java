/*
 * Copyright  2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

/**
 * The states of execution including source parse, resolve, any multi-phased passes, through execution.
 * At some point break up if we introduce compilation.
 * <p>
 * Init
 * LoadRaw
 * Link/Resolve
 * Validate - complex validation beyond basic syntax
 *
 * @author Jim Coles
 */
public enum ExecState {

    /**
     * source files are opened and environment configured
     */
    INITIALIZED,

    /**
     * Parse from source into in-memory parse tree.
     */
    PARSED,

    /**
     * Do we need an initial in-memory tree or just a final AST tree?
     */
    LOADED,

    /**
     * Performs consistency and validity checks that the grammar can't check for, and resolves classes
     */
    LINKED,

    /**
     * Complete building the AST
     */
    EXECUTED

//    /**
//     * instruction set is chosen, for example java5 or pre java5
//     */
//    INSTRUCTION_SELECTION,
//
//    /**
//     * creates the binary output in memory
//     */
//    CLASS_GENERATION,
//
//    /**
//     * write the binary output to the file system
//     */
//    OUTPUT,
//
//    /**
//     * Perform any last cleanup
//     */
//    FINALIZATION

}
