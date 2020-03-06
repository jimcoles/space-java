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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * <p>A {@link Rule} is a symbolic expression that represents a relation between
 * otherwise independent Variables (although in a degenerative case, a Rule
 * may reference one or zero Variables). An Rule, notionally,
 * can be a Constraint, a grammar Production, or a (mathematical) Equation.
 *
 * <p>By default, an Equation must at all times evaluate to true if the system is
 * to be deemed in a valid state. However, we will likely give the programmer a means
 * of activating/deactivating Equations dynamically.
 *
 * <p>Structurally, an Equation is just a boolean-valued {@link OperatorExpr} that
 * must evaluate to true.
 *
 * <p>Sub-types and usages:
 * <li>A QueryImpl uses a Rule to represent it's Filter (selection).
 *
 * @author Jim Coles
 * @version 1.0
 */
public class RuleImpl extends NamedElement implements Rule {

    private Projection varSpace;
    /** Must be boolean-valued. */
    private OperatorExpr operatorExpr;

    public RuleImpl(SourceInfo sourceInfo, String name, Projection varSpace,
                    OperatorExpr operatorExpr)
    {
        super(sourceInfo, name);
        this.varSpace = varSpace;
        this.operatorExpr = operatorExpr;
    }

    public RuleImpl(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
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
}
