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

import org.jkcsoft.space.lang.runtime.Executor;

/**
 * Used for simple operator evals such as numeric add, sub, comparison, etc.
 * @author Jim Coles
 */
public interface OperEvaluator {

    Assignable eval(Assignable ... args);

}
