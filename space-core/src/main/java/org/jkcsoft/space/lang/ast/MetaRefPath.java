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

import org.jkcsoft.java.util.Lister;
import org.jkcsoft.java.util.Strings;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a static path through a Space Namespace of meta objects
 * including directories, type definitions, and type members (datums and functions).
 * The last ref may be to a datum, a function, or a type depending on
 * the usage.
 *
 *   [dir1.][dirn.][t1.][d1]
 *
 * @author Jim Coles
 */
public class MetaRefPath {

    private ExpressionChain parentChain;
    private LinkedList<MetaRef> links = new LinkedList<>();

    MetaRefPath(ExpressionChain parentChain) {
        this.parentChain = parentChain;
    }

    void addLink(MetaRef link) {
        links.add(link);
    }

    public List<MetaRef> getLinks() {
        return links;
    }

    public Named getResolvedMetaObj() {
        return getLastLink().getResolvedMetaObj();
    }

    public MetaRef getLastLink() {
        return links.getLast();
    }

    public String getFullUrlSpec() {
        return (parentChain.getNsRefPart() != null ? parentChain.getNsRefPart().getNameExprText() + ":" : "") + getUrlPathSpec();
    }

    public String getUrlPathSpec() {
        return Strings.buildDelList(links, (Lister<MetaRef>) link -> link.toUrlString(), ".");
    }

}
