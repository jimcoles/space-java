/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime.jnative.space;

import org.jkcsoft.space.lang.ast.SpaceDefn;
import org.jkcsoft.space.lang.instance.*;

/**
 * @author Jim Coles
 */
public class SpaceOpers {

    public static Space newSpace(ObjectBuilder objectBuilder, Space context, SpaceDefn defn) {
        return objectBuilder.newSpace(context, defn);
    }

    /** */
    public static Tuple newTuple(ObjectBuilder objectBuilder, Space space, ScalarValue ... args) {
        Tuple tuple = objectBuilder.newTuple(space, args);

        return tuple;
    }

    /** */
    public static CharacterSequence newCharSequence(ObjectBuilder objectBuilder, String chars) {
        return objectBuilder.newCharacterSequence(chars);
    }

    public static SpaceOid assignment(SpaceOid leftOid, SpaceObject rightObject) {
        if (rightObject instanceof ScalarValue) {

        }
        else if (rightObject instanceof Tuple) {

        }
        else if (rightObject instanceof Space) {

        }
        return leftOid;
    }
}
