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
 * @author Jim Coles
 */
public class AssignmentExpr extends AbstractModelElement implements ValueExpr {

    private ExpressionChain leftSideDatumRef;
    private ValueExpr rightSideValueExpr;

    public AssignmentExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public ExpressionChain getLeftSideDatumRef() {
        return leftSideDatumRef;
    }

    /** The left-side reference. */
    public void setLeftSideDatumRef(ExpressionChain leftSideDatumRef) {
        this.leftSideDatumRef = leftSideDatumRef;
        //
        addChild(leftSideDatumRef);
        //
//        addReference(memberRef);
    }

    public void setRightSideValueExpr(ValueExpr rightSideValueExpr) {
        this.rightSideValueExpr = rightSideValueExpr;
        //
        if (rightSideValueExpr instanceof AbstractModelElement)
            addChild(((AbstractModelElement) rightSideValueExpr));

//        if (valueExpr instanceof MetaReference)
//            addReference(((MetaReference) valueExpr));

    }

    public ValueExpr getRightSideValueExpr() {
        return rightSideValueExpr;
    }

    public boolean isLhsExpr(ExpressionChain exprChain) {
        return exprChain == leftSideDatumRef;
    }

    @Override
    public boolean hasRef() {
        return false;
    }

    @Override
    public MetaRef getRef() {
        return null;
    }

    @Override
    public boolean isValueExpr() {
        return true;
    }

    @Override
    public DatumType getDatumType() {
        return leftSideDatumRef.getDatumType();
    }

    @Override
    public boolean hasResolvedType() {
        return leftSideDatumRef.hasResolvedType();
    }
}
