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

import org.jkcsoft.space.lang.ast.VariableDefn;
import org.jkcsoft.space.lang.runtime.RuntimeException;

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
public class Tuple extends SpaceObject implements Assignable {

    /** The Space to which this Tuple belongs. The Space may be anonymous. */
    private Space space;

//    private List<Assignable>    assignables = new LinkedList<>();
//    private List<ScalarValue>   values = new LinkedList<>();
//    private List<Association>   associations = new LinkedList<>();

    // variables in definition order
    private List<Variable> variables = new LinkedList<>();
    // seminal map
    private Map<String, Assignable> indexAllByName = new HashMap<>();
    // redundant
    private Map<String, ScalarValue> indexValuesByName = new HashMap<>();
    private Map<String, Association> indexAssociationsByName = new HashMap<>();

    Tuple(SpaceOid oid, Space space) {
        super(oid);
        this.space = space;
        List<VariableDefn> varDefnList = getVarDefnList();
        for (VariableDefn variableDefn : varDefnList) {
            variables.add(new Variable(this, variableDefn, null));
        }
    }

    Tuple(SpaceOid oid, Space space, Assignable ... assignables) {
        this(oid, space);
//        this.assignables.addAll(Arrays.asList(assignables));
        // set values based on order of variable and assoc in space type definition
        if (assignables.length > getVarDefnList().size())
            throw new RuntimeException("Too many variables for this Space.");
        for (int idxVar = 0; idxVar < assignables.length; idxVar++) {
            setValue(getVarNameAt(idxVar), assignables[idxVar]);
        }
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Space getSpace() {
        return space;
    }

    public Tuple setValue(String name, Assignable value) {
        indexAllByName.put(name, value);
        return this;
    }

    public Tuple setValue(int idx, Assignable value) {
        indexAllByName.put(getVarNameAt(idx), value);
        return this;
    }

//    private Tuple addValue(ScalarValue scalarValue) {

    /**
     * Return value of Tuple variable with <code>name</code>
     */
    public ScalarValue getValue(String name) {
        return indexValuesByName.get(name);
    }

    public ScalarValue getValueAt(int index) {
        return indexValuesByName.get(getVarNameAt(index));
    }

    public Assignable getAssignableAt(int index) {
        return indexAllByName.get(getVarNameAt(index));
    }

//    public List<Assignable> getAssignables() {
//        return assignables;
//    }

    public List<ScalarValue> getValues() {
        List<VariableDefn> varDefnList = getVarDefnList();
        List<ScalarValue> values = new ArrayList<>(varDefnList.size());
        varDefnList.forEach(variableDefn -> {
            values.add(getValue(variableDefn.getName()));
        });
        return values;
    }

    public SpaceOid getReferenceOid(String name) {
        return indexAssociationsByName.get(name).getReferenceOid();
    }

    public Association getAssoc(String name) {
        return indexAssociationsByName.get(name);
    }

//    public List<Association> getAssociations() {
//        return associations;
//    }

    private String getVarNameAt(int idxVar) {
        List<VariableDefn> variableDefnList = getVarDefnList();
        return variableDefnList.get(idxVar).getName();
    }

    private List<VariableDefn> getVarDefnList() {
        return space.getDefinition().getVariableDefnList();
    }
}
