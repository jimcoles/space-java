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
 * Base class for all things defined in source code, structures/type defs, callables
 * etc..
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class ModelElement extends SpaceObject {

    private ModelElement        parent;
    private SourceInfo sourceInfo;
    private List<ModelElement>  children = new LinkedList<>();
    //
    private Map<String, NamedElement> namedChildMap = new TreeMap<>();
    private Set<MetaReference> references = null;
    private List<ModelElement> groupingNodes = new LinkedList<>();

//    ModelElement() {
//        this(null);
//    }

    ModelElement(SourceInfo sourceInfo) {
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

    ModelElement addChild(ModelElement child) {
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

    public List<ModelElement> getChildren() {
        return children;
    }

    /** Must be called by the adder method for children, e.g., {@link SpaceTypeDefn}.addAssocDefn()
     * should call this for the {@link SpacePathExpr} associated with it's 'from' and 'to'
     * type definition. */
    private void addReference(MetaReference reference) {
        if (reference == null) throw new IllegalArgumentException("attempt to add null reference");
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

    public String getText() {
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

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + ":" + this.getOid() + "] " + "\"" + this.getText() + "\""
            + " (" + getSourceInfo() + ")";
    }

}