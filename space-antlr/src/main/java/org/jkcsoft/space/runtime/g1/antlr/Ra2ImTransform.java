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
public class Ra2ImTransform implements RaTreeListener {

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
        if (thisImNode.getType() == ImTreeNode.NodeType.DELIMETER) {
            log.debug("ignoring delimeter start");
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
                if (pending.getType() == ImTreeNode.NodeType.SPACE_DEF) {
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
                tni = new ImTreeNode(toNodeType(ruleName), ruleName);
                if (tni.getType() != ImTreeNode.NodeType.ATOM)
                    tni.setComplete(true);
            }
            else if (raTreeContext instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) raTreeContext;
                Token symbol = terminalNode.getSymbol();
                if (DELIMETERS.contains(symbol.getText()))
                    tni = new ImTreeNode(ImTreeNode.NodeType.DELIMETER, symbol.getText());
                else
                    tni = new ImTreeNode(ImTreeNode.NodeType.IDENTIFIER, symbol.getText());

            }
            else if (raTreeContext instanceof ErrorNode) {
                System.err.println("ANTLR error node found in tree eval: " + raTreeContext.toString());
            }
        }
        else {
            throw new IllegalStateException("Space ANTRL parse tree has an empty rule index.");
        }
        return tni;
    }

    private void coalesceImNodes(ImTreeNode leftNode, ImTreeNode rightNode) {
        if (leftNode.getType() == ImTreeNode.NodeType.ATOM
                || leftNode.getType() == ImTreeNode.NodeType.STRING_LITERAL)
        {
            if (rightNode.getType() == ImTreeNode.NodeType.STRING_LITERAL) {
                leftNode.setType(ImTreeNode.NodeType.STRING_LITERAL);
            }
            else if (rightNode.getType() == ImTreeNode.NodeType.IDENTIFIER) {
                leftNode.setText(rightNode.getText());
                leftNode.setComplete(true);
            }
        }
    }


    private ImTreeNode.NodeType toNodeType(String ruleName) {
        ImTreeNode.NodeType nt = null;
        ImTreeNode.NodeType[] enumConstants = ImTreeNode.NodeType.class.getEnumConstants();

        for (ImTreeNode.NodeType nodeType: enumConstants) {
            if (nodeType.getAntrlRuleName() != null
                && nodeType.getAntrlRuleName().equals(ruleName))
            {
                nt = nodeType;
                break;
            }
        }
        if (nt == null)
            throw new IllegalArgumentException(
                    "ANTLR rule name [" + ruleName + "] does not map to enumerated values.");

        return nt;
    }

}
