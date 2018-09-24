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
public class ValueExprChain implements ValueExpr {

    private List<ValueExpr> chain;

    public ValueExprChain addValueExpr(ValueExpr valueExpr) {
        if (chain == null)
            chain = new LinkedList<>();
        chain.add(valueExpr);
        return this;
    }

    public List<ValueExpr> getChain() {
        return chain;
    }
}
