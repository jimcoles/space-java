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
 * @param <T> The class of meta object being referenced.
 * @author Jim Coles
 */
public class MetaReference<T extends NamedElement> extends ModelElement implements ValueExpr {

    private NamedElement lexicalContext;
    private SpacePathExpr spacePathExpr;
    private T resolvedMetaObj;
    private LoadState state = LoadState.INITIALIZED;

    public MetaReference(SpacePathExpr spacePathExpr) {
        super(spacePathExpr.getSourceInfo());
        this.spacePathExpr = spacePathExpr;
    }

    public LoadState getState() {
        return state;
    }

    public void setState(LoadState state) {
        this.state = state;
    }

    public NamedElement getLexicalContext() {
        return lexicalContext;
    }

    public void setLexicalContext(NamedElement lexicalContext) {
        this.lexicalContext = lexicalContext;
    }

    public SpacePathExpr getSpacePathExpr() {
        return spacePathExpr;
    }

    public T getResolvedMetaObj() {
        return resolvedMetaObj;
    }

    public void setResolvedMetaObj(T resolvedMetaObj) {
        this.resolvedMetaObj = resolvedMetaObj;
    }

    public String getText() {
        return spacePathExpr.toString() + "(" + (state == LoadState.RESOLVED ? "res" : "unres") + ")";
    }

    @Override
    public String toString() {
        return "<" +
            spacePathExpr +
            " " + state +
            " ctx=" + (lexicalContext != null ? lexicalContext : "") +
            " obj=" + (resolvedMetaObj != null ? resolvedMetaObj : "") +
            '>';
    }
}
