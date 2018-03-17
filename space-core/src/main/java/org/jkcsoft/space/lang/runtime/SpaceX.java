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

/**
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceX extends RuntimeException {

    private RuntimeError error;

    public SpaceX(RuntimeError error) {
        super(error.toString());
        this.error = error;
    }

    public SpaceX(String message) {
        super(message);
    }

    public SpaceX(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeError getError() {
        return error;
    }
}
