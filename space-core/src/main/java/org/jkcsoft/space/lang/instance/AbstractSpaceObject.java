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

import org.jkcsoft.space.lang.ast.ComplexTypeImpl;
import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.VariableDeclImpl;

/**
 * Something is a {@link AbstractSpaceObject} if it is an identifiable thing.
 * There are only five kinds of SpaceObjects:
 * <p>
 * <table>
 *     <tr><th>Object Kind</th><th>Dims</th><th>Meta Type</th></tr>
 *     <tr><td>{@link ScalarValue}</td> <td>1x1</td> <td>{@link VariableDeclImpl}</td></tr>
 *     <tr><td>{@link TupleImpl}</td> <td>1xn</td> <td>{@link ComplexTypeImpl} or {@link org.jkcsoft.space.lang.ast.ViewDefn}</td></tr>
 *     <tr><td>{@link TupleSetImpl}</td> <td>nxm</td> <td>{@link ComplexTypeImpl}</td></tr>
 *     <tr>
 *         <td>TupleStream &nbsp&nbsp</td>
 *         <td>nx1 </td>
 *         <td>{@link org.jkcsoft.space.lang.ast.StreamTypeDefn}</td>
 *     </tr>
 * </table>
 *
 * @author Jim Coles
 */
public class AbstractSpaceObject implements SpaceObject {

    private DatumType defn;
    private SpaceOid oid;

    public AbstractSpaceObject(SpaceOid oid, DatumType defn) {
        this.oid = oid;
        this.defn = defn;
    }

    @Override
    public DatumType getDefn() {
        return defn;
    }

    @Override
    public SpaceOid getOid() {
        return oid;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + this.getOid();
    }

}
