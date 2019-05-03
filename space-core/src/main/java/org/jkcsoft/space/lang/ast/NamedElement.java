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

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Named elements define the lexical structure of the AST -- the structure by which elements
 * such as type definitions, variables, and functions may be referenced.
 *
 * @author Jim Coles
 */
public abstract class NamedElement extends AbstractModelElement implements Named, Comparable<NamedElement> {

    private String name;
    private String description;
//    private String fqName;

    protected NamedElement(SourceInfo sourceInfo, String name) {
        super(sourceInfo);
        this.name = name;
    }

    @Override
    void setParent(AbstractModelElement parent) {
        super.setParent(parent);
//        fqName = null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNamed() {
        return name != null;
    }

    @Override
    public List<String> getFullNamePath() {
        List<String> forwardList = new LinkedList<>();
//        forwardList.add(this.getName());
        ModelElement node = this;
        while (node != null) {
            if (node instanceof NamedElement) {
                forwardList.add(((NamedElement) node).getName());
            }
            //
            node = node.getParent();
        }
        Collections.reverse(forwardList);
        return forwardList;
    }

    public String getFQName() {
//        if (fqName == null)
        return Strings.buildDelList(getFullNamePath(), Strings.TO_STRING_LISTER, "/");
//        return fqName;
    }

    public abstract MetaType getMetaType();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(NamedElement o) {
        if (!(this.isNamed() && o.isNamed()))
            throw new IllegalArgumentException("Space bug: cannot compare two objects " +
                                                   "(" + this + "," + o + ") unless both are named.");
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String getDisplayName() {
        return getFQName();
    }

    protected String toUrlString() {
        return getName();
    }
}
