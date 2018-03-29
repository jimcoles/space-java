/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.jnative.math;

import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.jnative.SpaceNative;

/**
 * @author Jim Coles
 */
@SpaceNative
public class JnMath {

    public static Assignable addNum(Assignable... args) {
        Assignable value = null;
        Assignable left = args[0];
        Assignable right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJvalue() + ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJvalue() + ((RealValue) right).getJvalue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() + ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() + ((RealValue) right).getJvalue());
        }
        return value;
    }

    public static Assignable subNum(Assignable... args) {
        Assignable value = null;
        Assignable left = args[0];
        Assignable right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJvalue() - ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJvalue() - ((RealValue) right).getJvalue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() - ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() - ((RealValue) right).getJvalue());
        }
        return value;
    }

    public static Assignable multNum(Assignable... args) {
        Assignable value = null;
        Assignable left = args[0];
        Assignable right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJvalue() * ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJvalue() * ((RealValue) right).getJvalue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() * ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() * ((RealValue) right).getJvalue());
        }
        return value;
    }

    public static Assignable divNum(Assignable... args) {
        Assignable value = null;
        Assignable left = args[0];
        Assignable right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJvalue() / ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJvalue() / ((RealValue) right).getJvalue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() / ((CardinalValue) right).getJvalue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJvalue() / ((RealValue) right).getJvalue());
        }
        return value;
    }
    // Boolean
    public static Assignable and(Assignable... args) {
        return getObjFactory()
            .newBooleanValue(((BooleanValue) args[0]).getJvalue() & ((BooleanValue) args[1]).getJvalue());
    }
    public static Assignable condAnd(Assignable... args) {
        return getObjFactory()
            .newBooleanValue(((BooleanValue) args[0]).getJvalue() && ((BooleanValue) args[1]).getJvalue());
    }
    // Num comparison returns boolean
    public static Assignable equal(Assignable... args) {
        return getObjFactory()
            .newBooleanValue(((ScalarValue) args[0]).getJvalue() == ((ScalarValue) args[1]).getJvalue());
    }
    public static Assignable lt(Assignable... args) {
        Assignable left = args[0];
        Assignable right = args[1];
        boolean javaBool = false;
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                javaBool = ((CardinalValue) left).getJvalue() < ((CardinalValue) right).getJvalue();
            else if (right instanceof RealValue)
                javaBool = ((CardinalValue) left).getJvalue() < ((RealValue) right).getJvalue();
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                javaBool = ((RealValue) left).getJvalue() < ((CardinalValue) right).getJvalue();
            else if (right instanceof RealValue)
                javaBool = ((RealValue) left).getJvalue() < ((RealValue) right).getJvalue();
        }

        return getObjFactory().newBooleanValue(javaBool);
    }
    //
    private static ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }
}
