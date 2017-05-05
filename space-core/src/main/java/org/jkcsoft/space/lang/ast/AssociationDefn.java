/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

    private SpaceDefn from;
    private int fromMult;   // Defaults to "many" if assoc is declared within a Space Type Defn.

    private SpaceDefn to;
    private int toMult;     // Defaults to 1 if assoc is declared within a Space Type Defn.

    AssociationDefn(String name, SpaceDefn from, SpaceDefn to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    public SpaceDefn getFrom() {
        return from;
    }

    public SpaceDefn getTo() {
        return to;
    }

    /**
     * In Space, this expression would go in an Equation expression.
     */
    public boolean isRecursive() {
        return from == to;
    }

}
