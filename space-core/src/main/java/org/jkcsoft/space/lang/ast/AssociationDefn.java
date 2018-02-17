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
 * Captures a wide range of relationships such as one-to-many, recursive.
 * Analogous to a foreign key relationship in RDB world or a simple
 * object reference in Java.
 *
 * @author J. Coles
 * @version 1.0
 */
public class AssociationDefn extends ModelElement {

    private SpacePathExpr fromPath;
    private SpaceTypeDefn from;
    private int fromMult;   // Defaults to "many" if assoc is declared within a Space Type Defn.

    private SpacePathExpr toPath;
    private SpaceTypeDefn to;
    private int toMult;     // Defaults to 1 if assoc is declared within a Space Type Defn.

    AssociationDefn(String name, SpacePathExpr fromPath, SpacePathExpr toPath) {
        super(name);
        this.from = from;
        this.to = to;
    }

    public SpaceTypeDefn getFrom() {
        return from;
    }

    public SpaceTypeDefn getTo() {
        return to;
    }

    public SpacePathExpr getFromPath() {
        return fromPath;
    }

    public int getFromMult() {
        return fromMult;
    }

    public SpacePathExpr getToPath() {
        return toPath;
    }

    public int getToMult() {
        return toMult;
    }

    /**
     * In Space, this expression would go in an Equation expression.
     */
    public boolean isRecursive() {
        return from == to;
    }

}
