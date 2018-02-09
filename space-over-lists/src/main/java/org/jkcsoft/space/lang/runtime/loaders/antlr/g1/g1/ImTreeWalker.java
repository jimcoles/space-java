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

import java.util.LinkedList;
import java.util.List;

/**
 * Walks the Space "Intermediate" tree and fires events to any listeners.  Used to
 * write pretty-print of the tree and also to build our AST.
 *
 * @author Jim Coles
 */
public class ImTreeWalker {

    private List<ImTreeListener> listeners = new LinkedList<>();

    public void addListener(ImTreeListener listener) {
        listeners.add(listener);
    }

    public void visitAll(ImTreeNode tree) {
        visit(tree);
    }

    private void visit(ImTreeNode node) {
        for (ImTreeListener listener: listeners) {
            listener.startNode(node);
        }

        for (int idxChild = 0; idxChild < node.childCount(); idxChild++) {
            visit(node.getChild(idxChild));
        }

        for (ImTreeListener listener: listeners) {
            listener.endNode(node);
        }
        return;
    }

}
