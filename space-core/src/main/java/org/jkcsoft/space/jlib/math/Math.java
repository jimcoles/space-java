/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.jlib.math;

import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.jlib.bindings.SpaceNative;

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
                    ((CardinalValue) left).getJavaValue() + ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJavaValue() + ((RealValue) right).getJavaValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() + ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() + ((RealValue) right).getJavaValue());
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
                    ((CardinalValue) left).getJavaValue() - ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJavaValue() - ((RealValue) right).getJavaValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() - ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() - ((RealValue) right).getJavaValue());
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
                    ((CardinalValue) left).getJavaValue() * ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJavaValue() * ((RealValue) right).getJavaValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() * ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() * ((RealValue) right).getJavaValue());
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
                    ((CardinalValue) left).getJavaValue() / ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((CardinalValue) left).getJavaValue() / ((RealValue) right).getJavaValue());
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() / ((CardinalValue) right).getJavaValue());
            else if (right instanceof RealValue)
                value =getObjFactory().newRealValue(
                    ((RealValue) left).getJavaValue() / ((RealValue) right).getJavaValue());
        }
        return value;
    }
    // Boolean
    public static Value and(Value... args) {
        return getObjFactory()
            .newBooleanValue(((BooleanValue) args[0]).getJavaValue().getJavaBool() & ((BooleanValue) args[1]).getJavaValue().getJavaBool());
    }
    public static Value condAnd(Value... args) {
        return getObjFactory()
            .newBooleanValue(((BooleanValue) args[0]).getJavaValue().getJavaBool() &&
                                 ((BooleanValue) args[1]).getJavaValue().getJavaBool());
    }
    // Num comparison returns boolean
    public static Value equal(Value... args) {
        return getObjFactory()
            .newBooleanValue(((ScalarValue) args[0]).getJavaValue() == ((ScalarValue) args[1]).getJavaValue());
    }
    public static Value lt(Value... args) {
        Value left = args[0];
        Value right = args[1];
        boolean javaBool = false;
        if (left instanceof CardinalValue) {
            if (right instanceof CardinalValue)
                javaBool = ((CardinalValue) left).getJavaValue() < ((CardinalValue) right).getJavaValue();
            else if (right instanceof RealValue)
                javaBool = ((CardinalValue) left).getJavaValue() < ((RealValue) right).getJavaValue();
        }
        else if (left instanceof RealValue){
            if (right instanceof CardinalValue)
                javaBool = ((RealValue) left).getJavaValue() < ((CardinalValue) right).getJavaValue();
            else if (right instanceof RealValue)
                javaBool = ((RealValue) left).getJavaValue() < ((RealValue) right).getJavaValue();
        }

        return getObjFactory().newBooleanValue(javaBool);
    }
    //
    private static ObjectFactory getObjFactory() {
        return ObjectFactory.getInstance();
    }
}
