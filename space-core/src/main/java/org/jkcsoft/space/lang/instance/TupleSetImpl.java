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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p> A {@link TupleSetImpl} is a collection of Tuple by-reference and is the
 * central notion of the Space language. A {@link TupleSetImpl} adheres to the
 * set-theoretic notion of a Relation. It is an instance-level notion of a
 * collection of Tuples the values of which are controlled by a type
 * definition. It is similar to a java.util.Set
 * or an RDB Table.
 *
 * <p>Java Analog: java.util.Set
 * <p>RDB Analog: Table, View, or query result set</p>
 *
 * @author Jim Coles
 * @version 1.0
 */
public class TupleSetImpl extends AbstractSpaceObject implements TupleSet, ReferenceValueHolder<ReferenceValue<Object>, Object>
{
    private Space contextSpace;
    private SetTypeDefn setTypeDefn;
    /** The controlling definition. */
    private ViewDefn viewDefn;

    /** The backing set. */
    private final Set<ReferenceValueHolder> tupleRefs = new HashSet<>();

    TupleSetImpl(SpaceOid oid, SetTypeDefn setTypeDefn) {
        super(oid, setTypeDefn);
        this.setTypeDefn = setTypeDefn;
    }

    public TupleSet addTuple(Tuple tuple) {
        // TODO Validate type
        addTupleRef(ObjectFactory.getInstance().newFreeReferenceHolder(this, tuple.getOid()));
        return this;
    }

    public TupleSet addTuple(SpaceOid oid) {
        // TODO Validate type
        addTupleRef(ObjectFactory.getInstance().newFreeReferenceHolder(this, oid));
        return this;
    }

    private TupleSet addTupleRef(ReferenceValueHolder referenceHolder) {
        tupleRefs.add(referenceHolder);
        return this;
    }

    private void validate(Tuple tuple) {

    }

    @Override
    public DatumDecl getDeclaration() {
        return  null;   // TODO Not sure what to return here.
//        return isSingleWrapper() ? tunull;
    }


    @Override
    public void setValue(ReferenceValue<Object> value) {
//        if (! (value instanceof ReferenceValue))
//            throw new SpaceX("Value is not a reference value {0}", value);
        if (isSingleWrapper())
            tupleRefs.iterator().next().setValue(value);
    }

    @Override
    public ValueCollection<FreeReferenceHolder<Object>, ReferenceValue<Object>, Object> addValue(
        FreeReferenceHolder<Object> inHolder)
    {
        FreeReferenceHolder<SpaceOid> thisHolder =
            getObjectFactory().newFreeReferenceHolder(this, inHolder.getValue());
        this.tupleRefs.add(thisHolder);
        return null;
    }

    @Override
    public TypeDefn getContainedObjectType() {
        //
        return setTypeDefn.getContainedElementType();
    }

    @Override
    public boolean isSequence() {
        return false;
    }

    @Override
    public boolean isSet() {
        return true;
    }

    @Override
    public boolean hasValue() {
        return isSingleWrapper() && tupleRefs.iterator().next() != null;
    }

    @Override
    public ReferenceValue<Object> getValue() {
        return isSingleWrapper() ? (ReferenceValue<Object>) tupleRefs.iterator().next().getValue() : null;
    }

    public boolean isSingleWrapper() {
        return tupleRefs.size() == 1;
    }

    @Override
    public Iterator iterator() {
        return tupleRefs.iterator();
    }

    @Override
    public String toString() {
        TypeDefn typeDefn = ((SetTypeDefn) getDefn()).getContainedElementType();
        return super.toString() + " => " + JavaHelper.EOL
            + ((typeDefn.getScalarDofs() == 1) ?
            "(value)" :
            Strings.buildDelList(
                typeDefn.getDatumDeclList(),
                obj -> "\"" + ((DatumDecl) obj).getNamePart() + "\"",
                "\t"))
            + JavaHelper.EOL
            + Strings.buildNewlineList(tupleRefs);
    }

    @Override
    public boolean isCollective() {
        return false;
    }

    @Override
    public boolean isTuple() {
        return false;
    }

}
