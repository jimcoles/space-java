/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import java.util.*;

/**
 * Conceptually, a Tuple is an element of a Relation (which is a Set of Tuples).
 * A Tuple is much like a row in a JDBC recordset.
 * Values in a Tuple can be retrieved in order or by the name of the variable.  A Tuple
 * may contain only Scalar values and Oid-based references to Tuples in other
 * Spaces.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Tuple extends SpaceObject {

    /** May be anonymous. */
    private Space space;

    private List<Assignable>    assignables = new LinkedList<>();
    private List<ScalarValue>   values = new LinkedList<>();
    private List<Association>   associations = new LinkedList<>();
    //
    private Map<String, ScalarValue> indexValuesByName = new HashMap<>();
    private Map<String, Association> indexAssociationsByName = new HashMap<>();

    Tuple(SpaceOid oid, Space space, Assignable ... assignables) {
        super(oid);
        this.space = space;
        this.assignables.addAll(Arrays.asList(assignables));
        for (Assignable assignable : assignables) {
            if (assignable instanceof ScalarValue) {
                values.add((ScalarValue) assignable);
            }
            else if (assignable instanceof Association) {
                associations.add((Association) assignable);
            }
        }
        for (ScalarValue value: values) {
            indexValuesByName.put(value.getType().getName(), value);
        }
        for (Association association : associations) {
            if (association.getDefn() != null)
                indexAssociationsByName.put(association.getDefn().getName(), association);
        }
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Space getSpace() {
        return space;
    }

    public Tuple addValue(ScalarValue scalarValue) {
        values.add(scalarValue);
        assignables.add(scalarValue);
        indexValuesByName.put(
            getSpace().getDefinition().getVariableDefnAt(values.size()).getName(),
            scalarValue
        );
        return this;
    }

    public Tuple addReference(Association reference) {
        associations.add(reference);
        assignables.add(reference);
        indexAssociationsByName.put(reference.getDefn().getName(), reference);
        return this;
    }

    /**
     * Return value of Tuple variable with <code>name</code>
     */
    public ScalarValue getValue(String name) {
        return indexValuesByName.get(name);
    }

    public ScalarValue getValueAt(int index) {
        return values.get(index);
    }

    public Assignable getAssignableAt(int index) {
        return assignables.get(index);
    }

    public List<Assignable> getAssignables() {
        return assignables;
    }

    public List<ScalarValue> getValues() {
        return values;
    }

    public SpaceOid getReferenceOid(String name) {
        return indexAssociationsByName.get(name).getReferenceOid();
    }
    public List<Association> getAssociations() {
        return associations;
    }
}
