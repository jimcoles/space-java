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

    private MetaReference<NamedElement> memberRef;
    private ValueExpr valueExpr;

    public AssignmentExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public MetaReference<NamedElement> getMemberRef() {
        return memberRef;
    }

    public void setMemberRef(SpacePathExpr memberPath) {
        this.memberRef = new MetaReference<>(memberPath);
        //
        addChild(memberRef);
        //
        addReference(memberRef);
    }

    public void setValueExpr(ValueExpr valueExpr) {
        this.valueExpr = valueExpr;
        //
        if (valueExpr instanceof ModelElement)
            addChild(((ModelElement) valueExpr));

        if (valueExpr instanceof MetaReference)
            addReference(((MetaReference) valueExpr));

    }

    public ValueExpr getValueExpr() {
        return valueExpr;
    }


}
