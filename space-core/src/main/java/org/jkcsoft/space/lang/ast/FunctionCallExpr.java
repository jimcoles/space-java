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
 * The AST's representation of a call to an object.function() along with arguments expressions.
 *
 * @author Jim Coles
 */
public class FunctionCallExpr extends ModelElement implements ValueExpr {

    /**
     * The name of some other named design-time thing such as a function.
     */
    private NamePartExpr namePartExpr;
    private ValueExpr argValueExpr;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public FunctionCallExpr setFunctionRef(NamePartExpr namePartExpr)
    {
        if (namePartExpr == null) throw new RuntimeException("bug: function path null");
        //
        this.namePartExpr = namePartExpr;
        //
//        addChild(this.functionRef);
        //
        return this;
    }

    public NamePartExpr getNamePartExpr() {
        return namePartExpr;
    }

    public ValueExpr getArgValueExpr() {
        return argValueExpr;
    }

    public void setArgValueExpr(ValueExpr argValueExpr) {
        this.argValueExpr = argValueExpr;
        //
        addChild((ModelElement) argValueExpr);
    }

    @Override
    public String getDisplayName() {
        return namePartExpr.getDisplayName();
    }

    @Override
    public boolean hasRefName() {
        return true;
    }

    @Override
    public String getRefName() {
        return namePartExpr.getNameExpr();
    }

    @Override
    public DatumType getDatumType() {
        return null;
    }
}
