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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jim Coles
 */
public class MetaRefPath {

    private List<SimpleExprLink> links = new LinkedList<>();
    private ScopeKind resolvedDatumScope;

    MetaRefPath(ScopeKind resolvedDatumScope) {
        this.resolvedDatumScope = resolvedDatumScope;
    }

    void addLink(SimpleExprLink link) {
        links.add(link);
    }

    public NamedElement getResolvedMetaObj() {
        return getLastLink().getResolvedMetaObj();
    }

    public ScopeKind getResolvedDatumScope() {
        return resolvedDatumScope;
    }

    public SimpleExprLink getLastLink() {
        return links.get(links.size() - 1);
    }
}
