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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.SetTypeDefn;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * <p>"Everything is a Space" (TM).
 *
 * <p> A Space is a collection of Tuples and is the central notion of the Space
 * language. A Space, aka Relation, is an instance-level notion of a collection of Tuples the values of which are
 * controlled by a Space type definition. It is essentially similar to a collection in Java or a Table in RDB. A Space
 * may be derived from other Spaces. A Space may be a purely contextual such as the space of local variables within a
 * Function or Equation.
 *
 * <p>Java Analog: Array or java.util.Set
 * <p>RDB Analog: Table, View, or query result set</p>
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SetSpace extends SpaceObject implements Value, Space, Iterable<Tuple> {

    /** Can be an EntityDefn or a ViewDefn */
    private SetSpace contextSpace;

    /** The backing list maintains the sequence of tuples as they are added */
    private List<Tuple> tuples = new LinkedList<>();

    SetSpace(SpaceOid oid, SetSpace contextSpace, SetTypeDefn setTypeDefn) {
        super(oid, setTypeDefn);
        this.contextSpace = contextSpace;
    }

    public SetSpace addTuple(Tuple tuple) {
        tuples.add(tuple);
        validate(tuple);
        return this;
    }

    public Tuple getTupleAt(int idx) {
        return tuples.get(idx);
    }

    private void validate(Tuple tuple) {

    }

    public SetSpace getContextSpace() {
        return contextSpace;
    }

    @Override
    public Iterator<Tuple> iterator() {
        return tuples.iterator();
    }

    @Override
    public void forEach(Consumer<? super Tuple> action) {
        tuples.forEach(action);
    }

    @Override
    public Spliterator<Tuple> spliterator() {
        return null;
    }

    @Override
    public DatumType getType() {
        return getDefn();
    }

    public boolean isSingleWrapper() {
        return tuples.size() == 1 && tuples.get(0).isSingleWrapper();
    }
    @Override
    public Object getJvalue() {
        return tuples.get(0).getJvalue();
    }
}
