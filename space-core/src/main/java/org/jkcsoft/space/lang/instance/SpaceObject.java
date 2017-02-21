/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

/**
 * There are only five kinds of SpaceObjects:
 * {@link ScalarValue}
 * {@link Tuple}
 * {@link Space}
 * {@link BinarySequence}
 * {@link BinaryStream}
 *
 * @author Jim Coles
 */
public class SpaceObject {

    private SpaceOid oid;

    public SpaceObject(SpaceOid oid) {
        this.oid = oid;
    }

    public SpaceOid getOid() {
        return oid;
    }
}
