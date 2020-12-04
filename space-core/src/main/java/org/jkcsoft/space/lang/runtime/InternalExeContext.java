/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.instance.ValueHolder;

/**
 * @author Jim Coles
 */
public interface InternalExeContext extends ApiExeContext {

    void autoCastAssign(ValueHolder leftSideHolder, ValueHolder rightSideHolder);

    RuntimeError newRuntimeError(String msg);
}
