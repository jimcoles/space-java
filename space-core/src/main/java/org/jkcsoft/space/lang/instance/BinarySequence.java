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
 *
 * A {@link BinarySequence} is similar to the notion of an Array in other languages.
 * A sequence allows random get/set access to elements of the sequence, but does not
 * allow a direct 'append' to increase the size of the sequence.
 *
 * Similar to an array or string from other languages.
 *
 * @author Jim Coles
 */
public abstract class BinarySequence<T extends ScalarValue> extends AbstractSpaceObject implements Value, Referenceable {

    /** Limit constructor access to package-only. */
    BinarySequence(SpaceOid oid, StreamTypeDefn defn) {
        super(oid, defn);
    }

    public abstract T getElement(int index);

}
