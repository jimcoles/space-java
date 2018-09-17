/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.instance.SpaceObject;
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.*;

/**
 * Base class for all things defined in source code: type defs, function defs etc..
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class ModelElement extends SpaceObject {

    private ModelElement parent;
    private SourceInfo sourceInfo;
    /** If true, when resolving names, treat this node as if it's children were directly under this
     * node's parent. */
    private boolean isGroupingNode = false;

    // -------------- <start> redundant collections and state for fast lookup
    private int treeDepth = -1;
    private NamedElement namedParent;
    private List<ModelElement> children = new LinkedList<>();
    private Map<String, NamedElement> namedChildMap = new TreeMap<>();
    private Set<MetaReference> references = null;
    private List<ModelElement> groupingNodes = new LinkedList<>();
    // -------------- <end> redundant collections

    protected ModelElement(SourceInfo sourceInfo) {
        super(ObjectFactory.getInstance().newOid(), null);
        this.sourceInfo = sourceInfo;
    }

    public ModelElement getParent() {
        return parent;
    }

    /** Limit access to package. */
    void setParent(ModelElement parent) {
        this.parent = parent;
    }

    public boolean isGroupingNode() {
        return isGroupingNode;
    }

    public void setGroupingNode(boolean groupingNode) {
        isGroupingNode = groupingNode;
    }

    public ModelElement addChild(ModelElement child) {
        children.add(child);
        child.setParent(this);
        //
        if (child instanceof NamedElement) {
            NamedElement nChild = (NamedElement) child;
            namedChildMap.put(nChild.getName(), nChild);
        }
        if (AstUtils.isGroupingNode(child)) {
            groupingNodes.add(child);
        }
        if (child instanceof MetaReference) {
            addReference(((MetaReference) child));
        }
        //
        return child;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public List<ModelElement> getChildren() {
        return children;
    }

    /**
     * Must be called by the adder method for children, e.g., {@link SpaceTypeDefn}.addAssocDefn() should call this for
     * the {@link NamePartExpr} associated with it's 'from' and 'to' type definition.
     */
    private void addReference(MetaReference reference) {
        if (reference == null) throw new IllegalArgumentException("attempt to Add null reference");
        if (references == null)
            references = new HashSet<>();
        references.add(reference);
    }

    public boolean hasReferences() {
        return references != null && references.size() > 0;
    }

    public Set<MetaReference> getReferences() {
        return references;
    }

    public NamedElement getChildByName(String name) {
        return namedChildMap.get(name);
    }

    public String getDisplayName() {
        return "";
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasGroupingNodes() {
        return groupingNodes != null && groupingNodes.size() > 0;
    }

    public List<ModelElement> getGroupingNodes() {
        return groupingNodes;
    }

    public NamedElement getNamedParent() {
        // lazy init
        if (namedParent == null)
            namedParent = AstUtils.getNearestNamedParent(this);
        return namedParent;
    }

    public int getTreeDepth() {
        if (treeDepth == -1)
            treeDepth = (parent != null) ? 1 + getParent().getTreeDepth() : 0;

        return treeDepth;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + ":" + this.getOid() + "(" + getSourceInfo() + ")] "
            + "\"" + this.getDisplayName() + "\""
            + (AstUtils.isGroupingNode(this) ? " GROUP" : "")
            ;
    }
}