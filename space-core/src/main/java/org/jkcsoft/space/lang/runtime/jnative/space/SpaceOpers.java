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

import org.jkcsoft.space.lang.ast.SpaceTypeDefn;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.RuntimeException;

import java.text.MessageFormat;
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
        next = ((Tuple) current).getAssoc(assocName);
        if (next == null) {
            next = Executor.getInstance().dereference(((Tuple) current).getReferenceOid(assocName));
        }
        return next;
    }

    public static Space newSpace(ObjectBuilder objectBuilder, Space context, SpaceTypeDefn defn) {
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
     * Valid left/right:
     *
     * Left         Right
     * ------------------------
     * Variable     Variable
     * Association  Oid
     * Association  Association
     * Association  Tuple
     *
     */
    public static void assign(Executor exec, ObjectBuilder objectBuilder, Assignable leftAss, Assignable rightAss) {
//        SpaceObject leftSpaceObject = exec.dereference(leftOid);
//        if (rightObject instanceof ScalarValue) {
//            assert (leftSpaceObject instanceof ScalarValue);
//            Object newValueObject = ((ScalarValue) rightObject).getValue();
//            ((ScalarValue) leftSpaceObject).setValue(newValueObject);
//        }
//        else if (rightObject instanceof Tuple) {
//            if (leftSpaceObject instanceof Space) {
////                ((Space) leftSpaceObject);
//
//            }
//        }
//        else if (rightObject instanceof Space) {
//
//        }
        if (leftAss instanceof Variable) {
            if (rightAss instanceof Variable) {
                Variable leftVar = (Variable) leftAss;
                Variable rightVar = (Variable) rightAss;
                if (leftVar.getDefinition().getType() != rightVar.getDefinition().getType()) {
                    String msg = MessageFormat.format("Variable types incompatible. ${0} cannot be assigned to ${1}.",
                            rightVar.getDefinition().getType(), leftVar.getDefinition().getType());
                    throw new RuntimeException(msg);}
                leftVar.setScalarValue(rightVar.getScalarValue());
            }
        }

        return;
    }
}
