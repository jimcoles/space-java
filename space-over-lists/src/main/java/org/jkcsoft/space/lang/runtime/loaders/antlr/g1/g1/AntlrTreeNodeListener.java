/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr.g1.g1;

import org.antlr.v4.runtime.tree.Tree;

import java.util.List;

/**
 * @author Jim Coles
 */
public interface AntlrTreeNodeListener {

    void startNode(Tree treeContext, List<String> ruleNameIndex, int level);

    void endNode(Tree treeContext, List<String> ruleNameIndex, int level);

}
