/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.ModelElement;

/**
 * @author Jim Coles
 * @version 1.0
 */
public class RuntimeException extends java.lang.RuntimeException {

    public RuntimeException(String message) {
        super(message);
    }

    public RuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    public RuntimeException(String message, ModelElement elem, Throwable cause) {
        super(message, cause);
    }
}
