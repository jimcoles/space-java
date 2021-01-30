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

    public static void assignNoCast(EvalContext evalContext, ValueHolder leftSideHolder, ValueHolder rightSideHolder) {
        boolean assigned = false;
        RuntimeError error = null;
        if (leftSideHolder instanceof VariableValueHolder) {
            VariableValueHolder lsHolderAsVar = (VariableValueHolder) leftSideHolder;
            if (rightSideHolder instanceof ScalarValue) {
                ScalarValue rsScalarValue = (ScalarValue) rightSideHolder.getValue();
                if (rsScalarValue.getType() == lsHolderAsVar.getDeclaration().getType()) {
                    lsHolderAsVar.setValue(rsScalarValue);
                    assigned = true;
                }
                else {
                    error = evalContext.newRuntimeError(
                        "type mismatch: cannot assign " + lsHolderAsVar.getDeclaration().getType() + " <- " +
                            rsScalarValue.getType());
                }
            }
        }
        else if (leftSideHolder instanceof DeclaredReferenceHolder) {
            if (rightSideHolder instanceof DeclaredReferenceHolder) {
                leftSideHolder.setValue(rightSideHolder.getValue());
                assigned = true;
            }
            else if(rightSideHolder instanceof TupleImpl) {
                leftSideHolder.setValue(
                    ObjectFactory.getInstance().newReferenceByOid(((TupleImpl) rightSideHolder).getOid())
                );
                assigned = true;
            }
        }
        //
        if (!assigned) {
            if (error != null)
                throw new SpaceX(error);
            else
                throw new SpaceX(evalContext.newRuntimeError("cannot assign " + leftSideHolder + " <- " + rightSideHolder));
        }
    }

    public static SpaceX nosup(String msg) {
        return new SpaceX("method [{}}] not yet implemented", msg);
    }

}
