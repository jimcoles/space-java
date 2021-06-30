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

import org.jkcsoft.space.lang.instance.*;

/**
 * Bits of code used in various places.
 *
 * @author Jim Coles
 */
public class SpaceUtils {

    public static void assignNoCast(EvalContext evalContext, ValueHolder leftSideHolder, ValueHolder rightSideHolder) {
        boolean assigned = false;
        RuntimeError error = null;
        if (leftSideHolder.getType() == rightSideHolder.getType()) {
            leftSideHolder.setValue(rightSideHolder.getValue());
            assigned = true;
        }
        else {
            error = evalContext.newRuntimeError(
                "type mismatch: cannot assign " + leftSideHolder.getType() + " <- " +
                    rightSideHolder.getType());
        }
        //
        if (!assigned) {
            if (error != null)
                throw new SpaceX(error);
            else
                throw new SpaceX(evalContext.newRuntimeError("can not assign LHS <- RHS: " + leftSideHolder + " <- " + rightSideHolder));
        }
    }

    public static boolean assignNoCast(EvalContext evalContext, ValueHolder leftSideHolder, Tuple rhsTuple) {
        boolean assigned = false;
        if (leftSideHolder.getType() == rhsTuple.getType()) {
            leftSideHolder.setValue(
                ObjectFactory.getInstance().newReferenceByOid(rhsTuple.getOid())
            );
            assigned = true;
        }
        return assigned;
    }

    public static SpaceX nosup(String msg) {
        return new SpaceX("method [{}}] not yet implemented", msg);
    }

}
