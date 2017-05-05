/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;

/**
 * @author Jim Coles
 */
public class OperLookupExpr extends ModelElement implements ValueExpr {

    private OperEnum operLookupKey;

    public OperLookupExpr(OperEnum operLookupKey) {
        super(operLookupKey.toString());
        this.operLookupKey = operLookupKey;
    }

}
