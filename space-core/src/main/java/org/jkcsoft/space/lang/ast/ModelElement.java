/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

    ModelElement() {
        this(null);
    }

    ModelElement(String name) {
        super(ObjectBuilder.getInstance().newOid());
        this.name = name;
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

    public ModelElement addChild(ModelElement child) {
        children.add(child);
        return child;
    }

    public List<ModelElement> getChildren() {
        return children;
    }
}