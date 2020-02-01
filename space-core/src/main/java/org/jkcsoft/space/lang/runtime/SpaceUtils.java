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
 * Holds bits used in various places.
 *
 * @author Jim Coles
 */
public class SpaceUtils {

    public static void assignNoCast(Executor.EvalContext exe, ValueHolder leftSideHolder, Value rightSideValue) {
        boolean assigned = false;
        RuntimeError error = null;
        if (leftSideHolder instanceof Variable) {
            Variable lsHolderAsVar = (Variable) leftSideHolder;
            if (rightSideValue instanceof ScalarValue) {
                ScalarValue rsScalarValue = (ScalarValue) rightSideValue;
                if (rsScalarValue.getType() == lsHolderAsVar.getDeclaration().getType()) {
                    lsHolderAsVar.setValue(rsScalarValue);
                    assigned = true;
                }
                else {
                    error = exe.newRuntimeError(
                        "type mismatch: cannot assign " + lsHolderAsVar.getDeclaration().getType() + " <- " +
                            rsScalarValue.getType());
                }
            }
        }
//        else if (leftSideHolder instanceof ScalarValue) {
//            ScalarValue lsScalarValue = ((ScalarValue) leftSideHolder);
//            if (rightSideValue instanceof ScalarValue) {
//                ScalarValue rsScalarValue = (ScalarValue) rightSideValue;
//                if (rsScalarValue.getType() == lsScalarValue.getType()) {
//                    lsScalarValue.setJvalue(rsScalarValue.getJvalue());
//                    assigned = true;
//                }
//                else {
//                    error = exe.newRuntimeError(
//                        "type mismatch: cannot assign " + lsScalarValue.getType() + " <- " +
//                            rsScalarValue.getType());
//                }
//            }
//        }
        else if (leftSideHolder instanceof Reference) {
            if (rightSideValue instanceof Reference) {
                ((Reference) leftSideHolder).setToOid(((Reference) rightSideValue).getToOid());
                assigned = true;
            }
            else if(rightSideValue instanceof TupleImpl) {
                ((Reference) leftSideHolder).setToOid(((TupleImpl) rightSideValue).getOid());
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
