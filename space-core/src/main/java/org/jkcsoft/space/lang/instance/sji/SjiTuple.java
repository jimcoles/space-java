/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance.sji;

import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.instance.*;

/**
 * Wraps Java object to implement an AST Tuple.
 *
 * @author Jim Coles
 */
public class SjiTuple extends AbstractTuple implements Tuple {

    private SjiTypeDefn sjiTypeDefn;
    // the wrapped element
    private Object jObject;

    public SjiTuple(SpaceOid oid, SjiTypeDefn defn, Object jObject) {
        super(oid, defn);
        this.sjiTypeDefn = (SjiTypeDefn) defn;
        this.jObject = jObject;
    }

    public SjiTypeDefn getSjiTypeDefn() {
        return sjiTypeDefn;
    }

    public Object getjObject() {
        return jObject;
    }

    @Override
    public Object getJavaValue() {
        return jObject;
    }
}
