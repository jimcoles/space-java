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

import org.jkcsoft.space.lang.ast.StreamTypeDefn;

/**
 * A BinarySequence allows random get/set access to elements of the sequence, but does not
 * allow a direct 'append' to increase the size of the sequence.
 *
 * Similar to an array or string from other languages.
 *
 * @author Jim Coles
 */
public abstract class BinarySequence<T> extends SpaceObject {

    /** Limit constructor access to package-only. */
    BinarySequence(SpaceOid oid, StreamTypeDefn defn) {
        super(oid, defn);
    }

    abstract public T getElement(int index);

}
