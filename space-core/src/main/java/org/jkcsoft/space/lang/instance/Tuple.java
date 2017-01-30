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

import java.util.HashMap;
import java.util.Map;

/**
 * Conceptually, a Tuple is an element of a Relation (which is a Set). Much like a row in a JDBC recordset.
 * Values in a Tuple can be retrieved in order or by the name of the coordinate.  A Tuple
 * may contain only Scalar values but those values may be Oid-based references to other
 * Entities.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Tuple {

    private Space space;
    private ScalarValue[] values;
    //
    private Map<String, ScalarValue> indexValuesByName = new HashMap<>();

    Tuple(Space space, ScalarValue ... values) {
        this.space = space;
        this.values = values;
        for (ScalarValue value: values) {
            indexValuesByName.put(value.getType().getName(), value);
        }
    }

    public Space getSpace() {
        return space;
    }

    /**
     * Return value of Tuple variable with <code>name</code>
     */
    public ScalarValue getValue(String name) {
        return indexValuesByName.get(name);
    }

    public ScalarValue getValueAt(int index) {
        return values[index];
    }

    public void setSpace(Space space) {
        this.space = space;
    }
}
