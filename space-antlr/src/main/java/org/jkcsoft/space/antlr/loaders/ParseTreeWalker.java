/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.antlr.loaders;

import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
/**
 * @author Jim Coles
 */
public class ParseTreeWalker {

    private static final Logger log = LoggerFactory.getLogger(ParseTreeWalker.class);

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
