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
 * The AST's representation of a call to an object.function() along with arguments
 * as expressions.
 *
 * @author Jim Coles
 */
public class FunctionCallExpr extends AbstractModelElement implements NameRefOrHolder, ValueExpr {

    /**
     * The name of some other named design-time thing such as a function.
     */
    private SimpleNameRefExpr<FunctionDefn> functionRef;
    private ValueExpr argValueExpr;

    /**
     * Represents the invocation of a function.
     */
    FunctionCallExpr(SourceInfo sourceInfo) {
        super(sourceInfo);
    }

    public AbstractRefExpr getFunctionRef() {
        return functionRef;
    }

    public FunctionCallExpr setFunctionRef(SimpleNameRefExpr<FunctionDefn> namePartExpr)
    {
        if (namePartExpr == null) throw new RuntimeException("bug: function path null");
        //
        this.functionRef = namePartExpr;
        //
        addChild(this.functionRef);
        //
        return this;
    }

    public ValueExpr getArgValueExpr() {
        return argValueExpr;
    }

    public void setArgValueExpr(ValueExpr argValueExpr) {
        this.argValueExpr = argValueExpr;
        //
        addChild((AbstractModelElement) argValueExpr);
    }

    @Override
    public boolean hasRef() {
        return functionRef != null;
    }

    @Override
    public MetaRef getRef() {
        return functionRef;
    }

    @Override
    public boolean hasNameRef() {
        return functionRef != null;
    }

    @Override
    public NameRefOrHolder getNameRef() {
        return functionRef;
    }

    @Override
    public boolean isValueExpr() {
        return true;
    }

    @Override
    public boolean hasResolvedType() {
        return functionRef.hasResolvedType() &&
            functionRef.getResolvedMetaObj().getReturnType() != null;
    }

    @Override
    public TypeDefn getDatumType() {
        return functionRef.getResolvedMetaObj().getReturnType();
    }

    @Override
    public ByNameMetaRef<FunctionDefn> getRefAsNameRef() {
        return functionRef;
    }

    @Override
    public boolean hasTypedExpr() {
        return true;
    }

    @Override
    public TypedExpr getTypedExpr() {
        return this;
    }

    @Override
    public String getDisplayName() {
        return functionRef.getDisplayName();
    }

}
