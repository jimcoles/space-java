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

import org.jkcsoft.space.antlr.Space2Lexer;
import org.jkcsoft.space.lang.ast.SpaceDefn;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.Executor;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jim Coles
 */
public class SpaceOpers {

    private static Map<String, String> tokenToMethod = new TreeMap<>();

//    static {
//        Space2Lexer.ruleNames[Space2Lexer.AssignOper];
//    }

    public static SpaceObject nav(Tuple current, String assocName) {
        SpaceObject next = null;
        next = ((Tuple) current).getValue(assocName);
        if (next == null) {
            next = Executor.getInstance().dereference(((Tuple) current).getReferenceOid(assocName));
        }
        return next;
    }

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

    /**
     * Could get more complex if we auto-box and auto-unbox.
     * */
    public static SpaceOid assign(Executor exec, ObjectBuilder objectBuilder, SpaceOid leftOid, SpaceObject rightObject) {
        SpaceObject leftSpaceObject = exec.dereference(leftOid);
        if (rightObject instanceof ScalarValue) {
            assert (leftSpaceObject instanceof ScalarValue);
            Object newValueObject = ((ScalarValue) rightObject).getValue();
            ((ScalarValue) leftSpaceObject).setValue(newValueObject);
        }
        else if (rightObject instanceof Tuple) {
            if (leftSpaceObject instanceof Space) {
//                ((Space) leftSpaceObject);

            }
        }
        else if (rightObject instanceof Space) {

        }
        return leftOid;
    }
}
