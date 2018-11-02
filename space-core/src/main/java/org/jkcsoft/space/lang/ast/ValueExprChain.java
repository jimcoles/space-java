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
public class ValueExprChain extends ModelElement implements NamePath, ValueExpr {

    private List<ValueExpr> chain;

    ValueExprChain(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public ValueExprChain addValueExpr(ValueExpr valueExpr) {
        if (chain == null)
            chain = new LinkedList<>();
        chain.add(valueExpr);
        return this;
    }

    public List<ValueExpr> getChain() {
        return chain;
    }

    @Override
    public DatumType getDatumType() {
        return chain.get(chain.size() -1).getDatumType();
    }
}
