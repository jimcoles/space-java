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

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author Jim Coles
 */
public class Ra2ImTransform implements AntlrTreeNodeListener {

    private static Logger log = Logger.getRootLogger();

    private ImTreeNode imRoot;
    private ImTreeNode activeParent;
    private ImTreeNode pending;
    private ImTreeNode lastNode;
    private List<AstLoadError>  errors = new LinkedList<>();

//    private Map<String, Class<>>

    @Override
    public void startNode(Tree raTreeNode, List<String> ruleNameIndex, int level) {
        ImTreeNode thisImNode = getImNode(raTreeNode, ruleNameIndex);
        if (thisImNode.getType() == ImTreeNode.NodeType.TERMINAL) {
            log.debug("ignoring delimiter start");
            return;
        }

        if (pending == null) {
            pending = thisImNode;
            log.debug("starting new IM node: " + pending);
        }
        else {
            if (!pending.isComplete()) {
                coalesceImNodes(pending, thisImNode);
            }
            else
                log.error("a complete pending IM node was found upon start of new parse node");
        }

        // finish ...
        if (pending.isComplete()) {
            // tree management ...
            if (activeParent != null) {
                activeParent.addChild(pending);
                log.debug("added node to parent list; node path " + pending.getFullPath());
            }

            if (imRoot == null) {
                if (pending.getType() == ImTreeNode.NodeType.LIST) {
                    imRoot = pending;
                    activeParent = imRoot;
                    log.debug("starting IM space root node");
                }
                else
                    throw new IllegalStateException("first parse node type was not of type space");
            }
            else {
                if (pending.getType() == ImTreeNode.NodeType.LIST) {
                    activeParent = pending;
                    log.debug("new list starting; making it the active parent");
                }
            }
            pending = null;
        }
        lastNode = thisImNode;
    }

    @Override
    public void endNode(Tree treeContext, List<String> ruleNameIndex, int level) {
        ImTreeNode thisNode = getImNode(treeContext, ruleNameIndex);
        if (thisNode.getType() == ImTreeNode.NodeType.LIST) {
            log.debug("end of list so popping to parent IM node " + activeParent.getParent());
            activeParent = activeParent.getParent();
        }
        else {
            log.debug("ignore end of ["+thisNode.getType()+"]");
        }
    }

    private static final List<String> DELIMETERS = Arrays.asList("(", ")");

    private ImTreeNode getImNode(Tree raTreeContext, List<String> ruleNameIndex) {
        ImTreeNode tni = null;
        if (ruleNameIndex != null) {
            if (raTreeContext instanceof RuleContext) {
                RuleContext ruleContext = (RuleContext) raTreeContext;
                int ruleIndex = ruleContext.getRuleIndex();
                String ruleName = ruleNameIndex.get(ruleIndex);
                tni = new ImTreeNode(ImTreeNode.toNodeType(ruleName), ruleName);
                if (tni.getType() != ImTreeNode.NodeType.ATOM)
                    tni.setComplete(true);
            }
            else if (raTreeContext instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) raTreeContext;
                Token symbol = terminalNode.getSymbol();
                if (DELIMETERS.contains(symbol.getText()))
                    tni = new ImTreeNode(ImTreeNode.NodeType.TERMINAL, symbol.getText());
                else
                    tni = new ImTreeNode(ImTreeNode.NodeType.IDENTIFIER, symbol.getText());

            }
            else if (raTreeContext instanceof ErrorNode) {
                System.err.println("ANTLR error node found in tree exec: " + raTreeContext.toString());
            }
        }
        else {
            throw new IllegalStateException("Space ANTRL parse tree has an empty rule index.");
        }
        return tni;
    }


    /**
     * Collapses right node into left node.  Sets values of the left node
     * based on contents of the right node.
     *
     * ATOM <- STRING_LITERAL <- TERMINAL => STRING_LITERAL
     * ATOM <- TERMINAL => IDENTIFIER
     *
     * @param leftNode Existing node
     * @param rightNode New node
     */
    private void coalesceImNodes(ImTreeNode leftNode, ImTreeNode rightNode) {
        if (leftNode.getType() == ImTreeNode.NodeType.ATOM) {
            leftNode.setType(rightNode.getType());
        }
        else {
            leftNode.setText(rightNode.getText());
            leftNode.setComplete(true);
        }
    }


    public ImTreeNode getRootNode() {
        return imRoot;
    }
}
