/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.metameta;

import org.jkcsoft.space.antlr.Space2Lexer;

/**
 * @author Jim Coles
 */
public class Keywords {

    static {
    }

    public static void main(String[] args) {
        String displayName = Space2Lexer.VOCABULARY.getDisplayName(Space2Lexer.SPathNavAssocToOper);
        System.out.println(displayName);
    }
}
