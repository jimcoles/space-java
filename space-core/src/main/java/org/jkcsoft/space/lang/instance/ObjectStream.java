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
 * A stream of encoded objects per some object-to-stream protocol, e.g.,
 * Google Protocol Buffers or Java Serialization.
 *
 * Other Stream and Sequence types are specializations of an Object Stream.
 *
 * @author Jim Coles
 */
public class ObjectStream extends SpaceObject {

    public ObjectStream(SpaceOid oid, StreamTypeDefn defn) {
        super(oid, defn);
    }
}
