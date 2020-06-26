/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

/**
 * <p>A Space Program Model (SPM) is the Space counterpart to a conventional Abstract
 * Syntax Tree (AST). With an SPM, Space source code is loaded into normalized
 * Space object Tuples. The SpmExecutor accesses and traverses the SPM just as any
 * Space application would access its object space.
 *
 * <p>Representation as a Space also makes many compile-time operations easier such
 * as IDE code-completion support and lint-like rule-based code analysis.
 *
 * @author Jim Coles
 */
package org.jkcsoft.space.lang.runtime.spm;