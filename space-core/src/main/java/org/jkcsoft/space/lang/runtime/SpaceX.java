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

import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

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

    public SpaceX(String message, Object ... args) {
        super(args != null ? MessageFormatter.format(message, args).getMessage() : message);
    }

    public SpaceX(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean hasError() {
        return error != null;
    }

    public RuntimeError getError() {
        return error;
    }
}
