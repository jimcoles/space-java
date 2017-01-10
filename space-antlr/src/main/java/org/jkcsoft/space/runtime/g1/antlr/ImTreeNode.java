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

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class ImTreeNode {

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

    enum NodeType {
        UNKNOWN(null),
        SPACE_DEF("space"), // RULE
        LIST("list"),   // RULE
        ATOM("atom"),   // RULE
        STRING_LITERAL("string"),   // RULE
        IDENTIFIER(null),      // TERMINAL
        DELIMETER(null);       // TERMINAL/leaf symbol

        private String antrlRuleName;

        NodeType(String antlrRuleName) {
            this.antrlRuleName = antlrRuleName;
        }

        public String getAntrlRuleName() {
            return antrlRuleName;
        }
    }
}
