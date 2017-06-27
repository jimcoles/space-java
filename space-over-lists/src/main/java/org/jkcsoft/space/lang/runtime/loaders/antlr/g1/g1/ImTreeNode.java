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

import org.jkcsoft.space.antlr.SpaceListsParser;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class ImTreeNode {

    public static NodeType toNodeType(String ruleName) {
        NodeType nt = null;
        NodeType[] enumConstants = NodeType.class.getEnumConstants();

        for (NodeType nodeType: enumConstants) {
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

    // -------------------------------------------------------------------------

    private boolean complete = false;
    private NodeType type = NodeType.UNKNOWN;
    private String text;
    private ImTreeNode parent;
    private List<ImTreeNode> children;

    public ImTreeNode(NodeType nodeType, String text) {
        this.type = nodeType;
        this.text = text;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public NodeType getType() {
        return type;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete;
    }

    public ImTreeNode addChild(ImTreeNode child) {
        if (children == null)
            children = new LinkedList<>();
        children.add(child);
        child.setParent(this);
        return this;
    }

    public int childCount() {
        return (children != null) ? children.size() : 0;
    }

    public ImTreeNode getParent() {
        return parent;
    }

    public void setParent(ImTreeNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "[" + getFullPath() + "]" +
                " (" + type + ")" +
//                ", parent=" + parent +
                ", complete=" + complete +
//                ", children=" + children +
//                ", childCount=" + childCount() +
                '}';
    }

    public String getFullPath() {
        return ((getParent() != null) ?
                getParent().getFullPath() + ":" : "")
                + getText();
    }

    public ImTreeNode getChild(int idxChild) {
        return children.get(idxChild);
    }

    public int getLevel() {
        return (parent != null) ? parent.getLevel() + 1 : 0;
    }

    enum NodeType {
        UNKNOWN(-1),
//        SPACE_DEF(decodeRuleName(SpaceParser.RULE_list), true), // RULE
        LIST(SpaceListsParser.RULE_list, true),   // RULE
        ATOM(SpaceListsParser.RULE_atom),   // RULE
        STRING_LITERAL(SpaceListsParser.RULE_string),   // RULE
        IDENTIFIER(SpaceListsParser.RULE_identifier),      // RULE
        TERMINAL(-1),       // TERMINAL/leaf symbol
//        COMMENT(SpaceParser.RULE_comment)
        ;

        private int ruleIndex;
        private String antrlRuleName;
        private boolean isList = false;

        NodeType(int ruleIndex) {
            this(ruleIndex, false);
        }
        NodeType(int ruleIndex, boolean isList) {
            this.ruleIndex = ruleIndex;
            if (ruleIndex != -1)
                this.antrlRuleName = decodeRuleName(ruleIndex);
            this.isList = isList;
        }

        public String getAntrlRuleName() {
            return antrlRuleName;
        }

        public boolean isList() {
            return isList;
        }
    }

    private static String decodeRuleName(int idxRule) {
        return SpaceListsParser.ruleNames[idxRule];
    }
}
