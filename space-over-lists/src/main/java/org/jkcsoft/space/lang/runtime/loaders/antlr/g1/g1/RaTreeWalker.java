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

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;
import org.jkcsoft.space.antlr.SpaceParser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Walks the Space Antlr tree and fires events to any listeners.  Used to
 * write pretty-print of the tree and also to build our AST.
 *
 * @author Jim Coles
 */
public class RaTreeWalker {

    private List<AntlrTreeNodeListener> listeners = new LinkedList<>();

    public void addListener(AntlrTreeNodeListener listener) {
        listeners.add(listener);
    }

    public void visitAll(ParseTree tree, SpaceParser spaceParser) {
        String[] ruleNames = spaceParser != null ? spaceParser.getRuleNames() : null;
        List<String> ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
        visit(tree, ruleNamesList, 0);
    }

    private void visit(Tree treeContext, List<String> ruleNameIndex, int level) {
        for (AntlrTreeNodeListener listener: listeners) {
            listener.startNode(treeContext, ruleNameIndex, level);
        }

        int childLevel = level + 1;
        for (int idxChild = 0; idxChild < treeContext.getChildCount(); idxChild++) {
            visit(treeContext.getChild(idxChild), ruleNameIndex, childLevel);
        }

        for (AntlrTreeNodeListener listener: listeners) {
            listener.endNode(treeContext, ruleNameIndex, level);
        }
        return;
    }

}
