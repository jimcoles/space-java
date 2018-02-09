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

import org.jkcsoft.space.lang.instance.ObjectBuilder;
import org.jkcsoft.space.lang.instance.SpaceObject;
import org.jkcsoft.space.lang.instance.SpaceOid;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for all things defined in source code, structures/type defs, callables
 * etc..
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class ModelElement extends SpaceObject implements Named {

    private String              name;
    private String              description;
    private List<ModelElement>  children = new LinkedList<>();
    private ModelElement        parent;

    ModelElement() {
        this(null);
    }

    ModelElement(String name) {
        super(ObjectBuilder.getInstance().newOid());
        this.name = name;
    }

    public ModelElement getParent() {
        return parent;
    }

    /** Limit access to package. */
    void setParent(ModelElement parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isNamed() {
        return name != null;
    }

    public ModelElement setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ModelElement addChild(ModelElement child) {
        children.add(child);
        child.setParent(this);
        return child;
    }

    public List<ModelElement> getChildren() {
        return children;
    }

    public String getText() {
        return "";
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "] " +
                (getName() != null ? this.getName() : ("\"" + this.getText() + "\""));
    }
}