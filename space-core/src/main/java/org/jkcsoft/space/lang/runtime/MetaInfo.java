/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.NamedElement;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holds redundant info needed at runtime for lookup and navigation.
 * Primary use is lookup at design time and runtime:  For a given ModelElement, get the child
 * element with the specified element name.
 * <p>
 * This is not the structure to use for basic AST traversal by the interpreter.
 *
 * @author Jim Coles
 */
public class MetaInfo {
    // for this element
    private NamedElement keyElement;
    // we hold this info
    private Map<String, NamedElement> children = new TreeMap<>();

    public MetaInfo(NamedElement keyElement) {
        this.keyElement = keyElement;
    }

    void addChild(NamedElement child) {
        if (child.hasName())
            children.put(child.getName(), child);
        else
            throw new IllegalArgumentException("Bug in runtime: Child elements must be named.");
    }

    public NamedElement getChildByName(String name) {
        return children.get(name);
    }

    @Override
    public String toString() {
        return "("+keyElement.getName() + ": " + Strings.buildCommaDelList(children.keySet())+")";
    }
}
