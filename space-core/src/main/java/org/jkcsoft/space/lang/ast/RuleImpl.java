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

import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.metameta.MetaType;

/**
 *
 * @author Jim Coles
 * @version 1.0
 */
public class RuleImpl extends AbstractNamedElement implements Rule {

    private DatumProjectionExpr varSpace;
    /** Must be boolean-valued. */
    private OperatorExpr operatorExpr;

    public RuleImpl(SourceInfo sourceInfo, NamePart namePart, DatumProjectionExpr varSpace,
                    OperatorExpr operatorExpr)
    {
        super(sourceInfo, namePart);
        this.varSpace = varSpace;
        this.operatorExpr = operatorExpr;
    }

    public RuleImpl(SourceInfo sourceInfo, NamePart namePart) {
        super(sourceInfo, namePart);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.RULE;
    }

    /**
     * Returns true if this expression contains one "==" operator, no "<>" operators.
     */
    @Override
    public boolean isEquality() {
        return false;
    }

    @Override
    public Space getScopeSpace() {
        return null;
    }

    @Override
    public DatumProjectionExpr getVarSpace() {
        return varSpace;
    }

    @Override
    public OperatorExpr getOperatorExpr() {
        return operatorExpr;
    }
}
