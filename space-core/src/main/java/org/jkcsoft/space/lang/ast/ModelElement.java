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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Base class for all things defined in source code, structures/type defs, callables
 * etc..
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class ModelElement extends SpaceObject {

    private SourceInfo sourceInfo;
    private List<ModelElement>  children = new LinkedList<>();
    private ModelElement        parent;
    //
    private Set<MetaReference> references = null;

    ModelElement() {
        this(null);
    }

    ModelElement(SourceInfo sourceInfo) {
        super(ObjectFactory.getInstance().newOid());
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
        return child;
    }

    public List<ModelElement> getChildren() {
        return children;
    }

    /** Must be called by the adder method for children, e.g., {@link SpaceTypeDefn}.addAssociation()
     * should call this for the {@link SpacePathExpr} associated with it's 'from' and 'to'
     * type definition. */
    void addReference(MetaReference reference) {
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

    public String getText() {
        return "";
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + ":" + this.getOid() + "] " + "\"" + this.getText() + "\"";
    }

}