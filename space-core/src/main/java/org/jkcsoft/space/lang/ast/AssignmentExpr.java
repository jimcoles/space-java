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
public class AssignmentExpr extends ModelElement implements ValueExpr {

    private ExpressionChain memberRef;
    private ValueExpr valueExpr;

    public AssignmentExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public ExpressionChain getMemberRef() {
        return memberRef;
    }

    /** The left-side reference. */
    public void setMemberRef(ExpressionChain memberRef) {
        this.memberRef = memberRef;
        //
        addChild(memberRef);
        //
//        addReference(memberRef);
    }

    public void setValueExpr(ValueExpr valueExpr) {
        this.valueExpr = valueExpr;
        //
        if (valueExpr instanceof ModelElement)
            addChild(((ModelElement) valueExpr));

//        if (valueExpr instanceof MetaReference)
//            addReference(((MetaReference) valueExpr));

    }

    public ValueExpr getValueExpr() {
        return valueExpr;
    }

    @Override
    public DatumType getDatumType() {
        return memberRef.getDatumType();
    }

}
