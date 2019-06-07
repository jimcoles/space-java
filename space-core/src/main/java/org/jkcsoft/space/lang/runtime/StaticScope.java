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
import org.jkcsoft.space.lang.ast.ScopeKind;

/**
 * Bookkeeping structure that holds info during linker phase.
 *
 * @author Jim Coles
 */
public class StaticScope {

    private ModelElement context;
    private ScopeKind scopeKind;

    public StaticScope(ModelElement context) {
        setContext(context);
    }

    public StaticScope(ModelElement context, ScopeKind scopeKind) {
        this.context = context;
        this.scopeKind = scopeKind;
    }

    public void setContext(ModelElement context) {
        this.context = context;
        this.scopeKind = AstUtils.inferScopeKind(this.context);
    }

    public void setContext(ModelElement context, ScopeKind scopeKind) {
        this.context = context;
        this.scopeKind = scopeKind;
    }

    public ModelElement getContext() {
        return context;
    }

    public void setScopeKind(ScopeKind scopeKind) {
        this.scopeKind = scopeKind;
    }

    public ScopeKind getScopeKind() {
        return scopeKind;
    }
}
