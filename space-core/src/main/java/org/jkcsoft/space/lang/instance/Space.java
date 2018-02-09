/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.SpaceTypeDefn;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * "Everything is a Space".
 *
 * <p> A Space is a collection of Tuples and is the central notion of the Space language.
 * A Space, aka Relation, is an instance-level notion of a collection of Tuples the values
 * of which are controlled by a Space type definition. It is essentially similar to a
 * collection in Java or a Table in RDB. A Space may be derived from other Spaces.
 * A Space may be a purely contextual such as the space of local variables
 * within a Function or Equation.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Space extends SpaceObject implements Assignable, SpaceSet, Iterable<Tuple> {

    /** Can be an EntityDefn or a ViewDefn */
    private SpaceTypeDefn definition;
    private Space contextSpace;

    /** The backing list maintains the sequence of tuples as they are added */
    private List<Tuple> tuples = new LinkedList<>();

    Space(SpaceOid oid, Space contextSpace, SpaceTypeDefn definition) {
        super(oid);
        this.contextSpace = contextSpace;
        this.definition = definition;
    }

    public Space addTuple(Tuple tuple) {
        tuples.add(tuple);
        tuple.setSpace(this);
        validate(tuple);
        return this;
    }

    private void validate(Tuple tuple) {

    }

    public SpaceTypeDefn getDefinition() {
        return definition;
    }

    public Space getContextSpace() {
        return contextSpace;
    }

    @Override
    public Iterator<Tuple> iterator() {
        return tuples.iterator();
    }

    @Override
    public void forEach(Consumer<? super Tuple> action) {

    }

    @Override
    public Spliterator<Tuple> spliterator() {
        return null;
    }
}
