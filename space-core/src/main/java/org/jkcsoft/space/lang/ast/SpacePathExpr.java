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
 * Evaluates to a Space object which may be a meta (Definition) object.
 *
 * @author Jim Coles
 * @see OperLookupExpr
 */
public class SpacePathExpr extends ModelElement {

    private boolean isTypeExpr;  // If true, evaluates to a meta (definition) object.
    private PathOperEnum oper;
    private String text;
    private SpacePathExpr nextExpr;

    SpacePathExpr(SourceInfo sourceInfo, boolean isTypeExpr, PathOperEnum oper, String text, SpacePathExpr nextExpr) {
        super(sourceInfo);
        this.isTypeExpr = isTypeExpr;
        this.oper = oper;
        this.text = text;
        this.nextExpr = nextExpr;
    }

    public boolean isTypeExpr() {
        return isTypeExpr;
    }

    public PathOperEnum getOper() {
        return oper;
    }

    public boolean hasNextExpr() {
        return nextExpr != null;
    }

    public SpacePathExpr getNextExpr() {
        return nextExpr;
    }

    public void setNextExpr(SpacePathExpr nextExpr) {
        this.nextExpr = nextExpr;
    }

    public String getFullPath() {
        return "->" + text + (nextExpr != null ? nextExpr.getFullPath() : "");
    }

    public String getText() {
        return text;
    }
}
