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
 * @author Jim Coles
 */
public class ValueExprChain extends AbstractModelElement implements NamePath, ValueExpr {

    private List<ValueExpr> chain;

    ValueExprChain(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public ValueExprChain addValueExpr(ValueExpr valueExpr) {
        if (chain == null)
            chain = new LinkedList<>();
        chain.add(valueExpr);
        //
        addChild(valueExpr);

        return this;
    }

    public boolean isEmpty() {
        return chain == null || chain.isEmpty();
    }

    public List<ValueExpr> getChain() {
        return chain;
    }

    @Override
    public TypeDefn getDatumType() {
        return getLastExpr().getDatumType();
    }

    private ValueExpr getLastExpr() {
        return chain.get(chain.size() - 1);
    }

    @Override
    public boolean hasRef() {
        return getLastExpr().hasRef();
    }

    @Override
    public MetaRef getRef() {
        return getLastExpr().getRef();
    }

    @Override
    public boolean hasResolvedType() {
        return getLastExpr().hasResolvedType();
    }

    public int size() {
        return chain.size();
    }

    @Override
    public boolean isLiteral() {
        return chain.size() == 1 && chain.get(0).isLiteral();
    }
}
