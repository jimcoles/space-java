package org.jkcsoft.space.lang.runtime.loaders.antlr.g2;/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;

public class ParseTreeWalker {

    private static final Logger log = Logger.getLogger(ParseTreeWalker.class);

    public void walkTree(ParseTree rootNode) {
        visitNodeRecurse(rootNode);
    }

    private void visitNodeRecurse(ParseTree node) {
        //
        try {

        } catch (Exception e) {
            log.error("Failed loading. Could not transform [" + node + "]");
        }
        //
        int childCount = node.getChildCount();
        for(int idxChild = 0; idxChild < childCount; ++idxChild) {
            ParseTree childNode = node.getChild(idxChild);
            visitNodeRecurse(childNode);
        }
    }

    public interface WalkHandler {
        void onNode(ParseTree node);
    }
}
