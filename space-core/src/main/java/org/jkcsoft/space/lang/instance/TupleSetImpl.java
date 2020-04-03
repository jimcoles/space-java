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

import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.*;

import java.util.*;

/**
 * <p>"Everything is a Space" (TM).
 *
 * <p> A {@link TupleSetImpl} is a collection of Tuples and is the central notion of the Space
 * language. A {@link TupleSetImpl} adheres to the set-theoretic notion of a Relation.
 * It is an instance-level notion of a collection of Tuples the values of which are
 * controlled by a Space type definition. It is essentially similar to a java.util.TupleCollection
 * in Java or a Table in RDB. A Space may be derived from other Spaces. A Space may be a
 * purely contextual such as the space of local variables within a Function or Equation.
 *
 * <p>Java Analog: java.util.TupleCollection or Set
 * <p>RDB Analog: Table, View, or query result set</p>
 *
 * @author Jim Coles
 * @version 1.0
 */
public class TupleSetImpl extends AbstractSpaceObject implements TupleSet, ValueHolder {

    private Space contextSpace;
    private SetTypeDefn setTypeDefn;

    /** The backing list maintains the sequence of tuples as they are added */
    private List<FreeReferenceHolder> tupleRefs = new LinkedList<>();

    TupleSetImpl(SpaceOid oid, SetTypeDefn setTypeDefn) {
        super(oid, setTypeDefn);
        this.setTypeDefn = setTypeDefn;
    }

    public TupleSet addTuple(Tuple tuple) {
        addTupleRef(ObjectFactory.getInstance().newReferenceByOidHolder(tuple);
        return this;
    }

    public TupleSet addTupleRef(FreeReferenceHolder referenceHolder) {
        tupleRefs.add(referenceHolder);
        return this;
    }

    private void validate(Tuple tuple) {

    }


    @Override
    public Declaration getDeclaration() {
        return  null;   // TODO Not sure what to return here.
//        return isSingleWrapper() ? tunull;
    }

    @Override
    public Projection getType() {
        return getDefn();
    }

    @Override
    public ValueCollection<FreeReferenceHolder> addValue(ValueHolder holder) {
        return null;
    }

    @Override
    public void setValue(Value value) {
        if (isSingleWrapper())
            tupleRefs.get(0).setValue(value);
    }

    @Override
    public boolean hasValue() {
        return isSingleWrapper() && tupleRefs.get(0) != null;
    }

    @Override
    public Value getValue() {
//        ObjectFactory.getInstance()
        return isSingleWrapper() ? tupleRefs.get(0).get(0).getValue() : null;
    }

    public boolean isSingleWrapper() {
        return tupleRefs.size() == 1 && tupleRefs.get(0).isSingleValueWrapper();
    }

    @Override
    public String toString() {
        DatumType datumType = ((SetTypeDefn) getDefn()).getContainedElementType();
        return super.toString() + " => " + JavaHelper.EOL
            + ((datumType instanceof SimpleType) ?
            "(value)" :
            Strings.buildDelList(
                ((ComplexType) datumType).getDatumDeclList(),
                obj -> "\"" + ((Declaration) obj).getName() + "\"",
                "\t"))
            + JavaHelper.EOL
            + Strings.buildNewlineList(tupleRefs);
    }
}
