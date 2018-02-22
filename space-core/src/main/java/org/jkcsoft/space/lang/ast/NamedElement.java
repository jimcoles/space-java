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

import java.util.Map;
import java.util.TreeMap;

/**
 * Named elements define the lexical structure of the AST -- the structure by which elements
 * such as type definitions, variables, and functions may be referenced.
 *
 * @author Jim Coles
 */
public class NamedElement extends ModelElement implements Named, Comparable<NamedElement> {

    private String              name;
    private String              description;
    private Map<String, NamedElement> namedChildMap = new TreeMap<>();

    NamedElement(SourceInfo sourceInfo, String name) {
        super(sourceInfo);
        setName(name);
    }

    public ModelElement setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNamed() {
        return name != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    ModelElement addChild(ModelElement child) {
        if (child instanceof NamedElement) {
            NamedElement nChild = (NamedElement) child;
            namedChildMap.put(nChild.getName(), nChild);
        }
        return super.addChild(child);
    }

    public NamedElement getChildByName(String name) {
        return namedChildMap.get(name);
    }

    @Override
    public int compareTo(NamedElement o) {
        if (!(this.isNamed() && o.isNamed()))
            throw new IllegalArgumentException("Space bug: cannot compare two objects " +
                    "(" + this + "," + o +") unless both are named.");
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String getText() {
        return getName();
    }
}
