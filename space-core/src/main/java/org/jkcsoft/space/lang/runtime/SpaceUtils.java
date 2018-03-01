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

    public static Assignable assignOper(Tuple ctxObject, NamedElement member, Assignable rightSideValue) {
        // TODO Add some notion of 'casting' and 'auto-(un)boxing'.
        boolean assigned = false;
        Assignable leftSideHolder = ctxObject.get(member);
        if (leftSideHolder instanceof Variable) {
            if (rightSideValue instanceof ScalarValue) {
//                Executor.log.debug("setting scalar to scalar");
                ((Variable) leftSideHolder).setScalarValue((ScalarValue) rightSideValue);
                assigned = true;
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
        if (!assigned)
            throw new IllegalArgumentException("cannot assign " + rightSideValue + " to " + leftSideHolder) ;
        //
        return leftSideHolder;
    }
}
