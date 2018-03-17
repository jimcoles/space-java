/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.ModelElement;

/**
 * @author Jim Coles
 */
public class ReturnValue extends AbstractExeContext {

    private Assignable returnValue;

    public ReturnValue(ModelElement astNode) {
        super(astNode);
    }

    public Assignable getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Assignable returnValue) {
        this.returnValue = returnValue;
    }
}
