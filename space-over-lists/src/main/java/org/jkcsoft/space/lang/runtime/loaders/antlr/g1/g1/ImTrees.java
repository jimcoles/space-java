/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime.loaders.antlr.g1.g1;

/**
 * @author Jim Coles
 */
public class ImTrees {

    public static ImTreeNode first(ImTreeNode listNode) {
        if (!listNode.getType().isList())
            throw new IllegalArgumentException("node is not a list");
        return listNode.getChild(0);
    }

}
