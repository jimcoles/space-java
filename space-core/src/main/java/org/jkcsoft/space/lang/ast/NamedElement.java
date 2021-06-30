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

    private NamePart namePart;
    private String description;

    protected NamedElement(SourceInfo sourceInfo, NamePart namePart) {
        super(sourceInfo);
        this.namePart = namePart;
    }

    @Override
    public NamePart getNamePart() {
        return namePart;
    }

    public boolean hasName() {
        return namePart != null;
    }

    @Override
    public String getName() {
        return namePart.getText();
    }

    @Override
    public List<String> getFullNamePath() {
        List<String> forwardList = new LinkedList<>();
//        forwardList.add(this.getName());
        ModelElement node = this;
        while (node != null) {
            if (node instanceof NamedElement) {
                forwardList.add(((NamedElement) node).getNamePart().getText());
            }
            //
            node = node.getParent();
        }
        Collections.reverse(forwardList);
        return forwardList;
    }

    @Override
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
        if (!(this.hasName() && o.hasName()))
            throw new IllegalArgumentException("Space bug: cannot compare two objects " +
                                                   "(" + this + "," + o + ") unless both are named.");
        return this.getNamePart().compareTo(o.getNamePart());
    }

    @Override
    public String getDisplayName() {
        return getFQName();
    }

    protected String toUrlString() {
        return getNamePart().getText();
    }
}
