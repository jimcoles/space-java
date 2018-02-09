/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.lang.loader;

/**
 * @author Jim Coles
 */
public class AstLoadError {

    private String message;
    private int line;
    private int character;

    public AstLoadError(String message, int line, int character) {
        this.message = message;
        this.line = line;
        this.character = character;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }
}
