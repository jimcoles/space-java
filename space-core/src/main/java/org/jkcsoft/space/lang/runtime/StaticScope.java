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

    private AstScopeCollection scopeColl;
    private ModelElement context;
    private ScopeKind scopeKind;

    public StaticScope(AstScopeCollection scopeColl, ModelElement context) {
        this.scopeColl = scopeColl;
        setContext(context);
    }

    public StaticScope(AstScopeCollection scopeColl, ModelElement context, ScopeKind scopeKind) {
        this.scopeColl = scopeColl;
        setContext(context, scopeKind);
    }

    public AstScopeCollection getScopeColl() {
        return scopeColl;
    }

    public void setContext(ModelElement context, ScopeKind scopeKind) {
        this.context = context;
        this.scopeKind = scopeKind;
    }

    public ModelElement getContext() {
        return context;
    }

    public void setContext(ModelElement context) {
        setContext(context, AstUtils.inferScopeKind(this.context));
    }

    public ScopeKind getScopeKind() {
        return scopeKind;
    }

    public void setScopeKind(ScopeKind scopeKind) {
        this.scopeKind = scopeKind;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StaticScope && scopeKind == ((StaticScope) obj).getScopeKind();
    }

    @Override
    public String toString() {
        return "Scope [" +
            "\''" + scopeColl.getCollectionName() + '\'' +
            " " + scopeKind + " " +
            " AST Node " + context +
            ']';
    }
}
