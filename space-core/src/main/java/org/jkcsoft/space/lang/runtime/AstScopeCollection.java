/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Used by Linker to walk AST paths specific to a reference's scope. E.g., rules
 * for resolving new Tuple LHS var are different that those for general var
 * resolving. This interface avoids clunky case logic.
 *
 * @author Jim Coles
 */
public class AstScopeCollection extends LinkedList<StaticScope> {

    private String collectionName;

    public AstScopeCollection(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }

//    boolean hasNextScope();
//    StaticScope nextScope();
//    StaticScope getCurrentScope();

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AstScopeCollection))
            return false;
        return this.collectionName.equals(((AstScopeCollection) o).getCollectionName());
    }

    @Override
    public String toString() {
        return "AstScopeCollection(" + collectionName + ')';
    }
}
