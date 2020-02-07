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
public class Math {

    public static Value addNum(Value... args) {
        Value value = null;
        Value left = args[0];
        Value right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJValue() + ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJValue() + ((RealValue) right).getJValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() + ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() + ((RealValue) right).getJValue());
        }
        return value;
    }

    public static Value subNum(Value... args) {
        Value value = null;
        Value left = args[0];
        Value right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJValue() - ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJValue() - ((RealValue) right).getJValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() - ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() - ((RealValue) right).getJValue());
        }
        return value;
    }

    public static Value multNum(Value... args) {
        Value value = null;
        Value left = args[0];
        Value right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJValue() * ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJValue() * ((RealValue) right).getJValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() * ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() * ((RealValue) right).getJValue());
        }
        return value;
    }

    public static Value divNum(Value... args) {
        Value value = null;
        Value left = args[0];
        Value right = args[1];
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                value =getObjFactory().newCardinalValue(
                    ((CardinalValue) left).getJValue() / ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJValue() / ((RealValue) right).getJValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() / ((CardinalValue) right).getJValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJValue() / ((RealValue) right).getJValue());
        }
        return value;
    }
    // Boolean
    public static Value and(Value... args) {
        return getObjFactory()
            .newBooleanValue(((BooleanValue) args[0]).getJValue() & ((BooleanValue) args[1]).getJValue());
    }
    public static Value condAnd(Value... args) {
        return getObjFactory()
            .newBooleanValue(((BooleanValue) args[0]).getJValue() && ((BooleanValue) args[1]).getJValue());
    }
    // Num comparison returns boolean
    public static Value equal(Value... args) {
        return getObjFactory()
            .newBooleanValue(((ScalarValue) args[0]).getJValue() == ((ScalarValue) args[1]).getJValue());
    }
    public static Value lt(Value... args) {
        Value left = args[0];
        Value right = args[1];
        boolean javaBool = false;
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                javaBool = ((CardinalValue) left).getJValue() < ((CardinalValue) right).getJValue();
            else if (right instanceof RealValue)
                javaBool = ((CardinalValue) left).getJValue() < ((RealValue) right).getJValue();
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                javaBool = ((RealValue) left).getJValue() < ((CardinalValue) right).getJValue();
            else if (right instanceof RealValue)
                javaBool = ((RealValue) left).getJValue() < ((RealValue) right).getJValue();
        }

        return getObjFactory().newBooleanValue(javaBool);
    }
    //
    private static ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }
}
