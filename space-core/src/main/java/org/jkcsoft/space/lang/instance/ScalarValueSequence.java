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
 * Instances of the {@link ScalarValueSequence} class represent the Space
 * notion of a <code>byte[]</code>.
 *
 * @author Jim Coles
 */
public abstract class ScalarValueSequence<T extends ScalarValue> extends AbstractSequence<T> {

    /** Limit constructor access to package-only. */
    ScalarValueSequence(SpaceOid oid, StreamTypeDefn defn) {
        super(oid, defn);
    }

    public abstract T getElement(int index);

}
