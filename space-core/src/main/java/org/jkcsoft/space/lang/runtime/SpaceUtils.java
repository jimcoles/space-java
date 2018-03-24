/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.NamedElement;
import org.jkcsoft.space.lang.instance.*;

/**
 * Holds bits used in various places.
 *
 * @author Jim Coles
 */
public class SpaceUtils {

    public static void assignOper(Executor.EvalContext exe, Assignable leftSideHolder, Assignable rightSideValue) {
        boolean assigned = false;
        RuntimeError error = null;
        if (leftSideHolder instanceof Variable) {
            if (rightSideValue instanceof ScalarValue) {
//                Executor.log.debug("setting scalar to scalar");
                ScalarValue rsScalarValue = (ScalarValue) rightSideValue;
                Variable lsHolder = (Variable) leftSideHolder;
                if (rsScalarValue.getType() == lsHolder.getDefinition().getType()) {
                    lsHolder.setScalarValue(rsScalarValue);
                    assigned = true;
                }
                else {
                    error = exe.newRuntimeError(
                        "type mismatch: cannot assign " + lsHolder.getDefinition().getType() + " <- " +
                            rsScalarValue.getType());
                }
            }
        }
        else if (leftSideHolder instanceof Reference) {
            if (rightSideValue instanceof Reference) {
//                Executor.log.debug("setting reference to reference");
                ((Reference) leftSideHolder).setToOid(((Reference) rightSideValue).getToOid());
                assigned = true;
            }
        }
        //
        if (!assigned) {
            if (error != null)
                throw new SpaceX(error);
            else
                throw new SpaceX(exe.newRuntimeError("cannot assign " + leftSideHolder + " <- " + rightSideValue));
        }
        //
    }
}
