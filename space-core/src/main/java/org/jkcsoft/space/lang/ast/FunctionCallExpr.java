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
public class FunctionCallExpr extends ModelElement implements MemberRefExpr {

    /**
     * The name of some other named design-time thing such as a function.
     */
    private RefExprImpl<FunctionDefn> functionRef;
    private ValueExpr argValueExpr;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public RefExprImpl getFunctionRef() {
        return functionRef;
    }

    public FunctionCallExpr setFunctionRef(RefExprImpl namePartExpr)
    {
        if (namePartExpr == null) throw new RuntimeException("bug: function path null");
        //
        this.functionRef = namePartExpr;
        //
//        addChild(this.functionRef);
        //
        return this;
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
        return functionRef.getDisplayName();
    }

    @Override
    public boolean hasNameRef() {
        return true;
    }

    @Override
    public NameRef getNameRef() {
        return functionRef.getNameRef();
    }

    @Override
    public void setResolvedMetaObj(FunctionDefn resolvedMetaObj) {
        functionRef.setResolvedMetaObj(resolvedMetaObj);
    }

    @Override
    public void setState(LinkState linkState) {
        functionRef.setState(linkState);
    }

    @Override
    public void setTypeCheckState(TypeCheckState typeCheckState) {
        functionRef.setTypeCheckState(typeCheckState);
    }

    @Override
    public boolean isResolved() {
        return functionRef.isResolved();
    }

    @Override
    public DatumType getDatumType() {
        return functionRef.getResolvedMetaObj().getReturnType();
    }
}
