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

/**
 * An FunctionDefn is a composite sequence of imperative statements
 * along with local variables and associations.
 *
 * A FunctionDefn:
 * <ul>
 * <li>may be named or not (anonymous)
 * <li>may be callable (function) or not (nested)</li>
 * <li>may be automatically derived from an Equation (future feature)
 * <li>may be associated by the user with an Equation in which case the
 * Space designtime or runtime checks that the Equation is always true.
 *</ul>
 *
 * OOP Analog: Function or Method (definition)
 *
 * @author Jim Coles
 */
public class FunctionDefn extends AbstractFunctionDefn implements Callable {

    private SpaceTypeDefn localSpaceTypeDefn;
    private StatementBlock statementBlock;

    FunctionDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    // ------------------------------------------------------------------------
    // Child Adders

    public StatementBlock setStatementBlock(StatementBlock statementBlock) {
        this.statementBlock = statementBlock;
        //
        addChild(statementBlock);
        //
        return statementBlock;
    }

    public StatementBlock getStatementBlock() {
        return statementBlock;
    }
}
