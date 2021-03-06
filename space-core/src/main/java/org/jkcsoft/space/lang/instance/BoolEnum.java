/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * @author Jim Coles
 */
public enum BoolEnum {

    TRUE(Boolean.TRUE),
    FALSE(Boolean.FALSE),
    UNKNOWN(null);

    Boolean javaBool;

    BoolEnum(Boolean javaBool) {
        this.javaBool = javaBool;
    }

    public Boolean getJavaBool() {
        return javaBool;
    }
}
