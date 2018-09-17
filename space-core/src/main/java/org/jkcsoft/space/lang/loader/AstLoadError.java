/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.loader;

import org.jkcsoft.space.lang.ast.SourceInfo;

/**
 * @author Jim Coles
 */
public class AstLoadError {

    public enum Level {
        WARN,
        ERROR
    }

    public enum Type {
        PARSE_WARNING(Level.WARN),
        SYNTAX(Level.ERROR),
        LINK(Level.ERROR),
        SEMANTIC(Level.ERROR);

        private Level level;

        Type(Level level) {
            this.level = level;
        }

        public Level getLevel() {
            return level;
        }
    }

    private Type type;
    private SourceInfo sourceInfo;
    private String message;

    public AstLoadError(Type type, SourceInfo sourceInfo, String message) {
        this.type = type;
        this.message = message;
        this.sourceInfo = sourceInfo;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getLevel().toString() + " " + type.toString().toLowerCase() + " " + sourceInfo + " " + message;
    }
}
