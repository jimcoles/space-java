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
public class SpaceFunctionDefn extends AbstractFunctionDefn implements FunctionDefn {

    private StatementBlock statementBlock;

    SpaceFunctionDefn(SourceInfo sourceInfo, String name, TypeRef returnTypeRef) {
        super(sourceInfo, name, returnTypeRef);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public DatumType getReturnType() {
        return getReturnTypeRef().getResolvedMetaObj();
    }

    public StatementBlock setStatementBlock(StatementBlock statementBlock) {
        this.statementBlock = statementBlock;
//        this.statementBlock.setGroupingNode(true);
        //
        addChild(statementBlock);
        //
        return statementBlock;
    }

    public StatementBlock getStatementBlock() {
        return statementBlock;
    }
}
