/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.runtime.g1.antlr;

import org.antlr.v4.runtime.tree.Tree;

import java.util.List;

/**
 * @author Jim Coles
 */
public interface RaTreeListener {

    void startNode(Tree treeContext, List<String> ruleNameIndex, int level);

    void endNode(Tree treeContext, List<String> ruleNameIndex, int level);

}
