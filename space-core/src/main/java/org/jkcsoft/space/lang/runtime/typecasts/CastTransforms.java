/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.typecasts;

import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.EvalContext;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.SpaceUtils;
import org.jkcsoft.space.lang.runtime.SpaceX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Implement transforms from one type to another. These include
 * - Space to Java (NOTE: Executor logic uses the Java backing object from any SJI wrapper for calls into Java functions.)
 * - Java SJI wrapper to Space
 * - numeric cast: e.g., int to real, real to int
 * - implicit cast of scalars to charseq
 * - boxing of primitive types to wrapped types, including char[] <-> CharacterSequence
 *
 * @author Jim Coles
 */
public class CastTransforms {

    private static final Logger log = LoggerFactory.getLogger(CastTransforms.class);

    private Map<TypeDefn, Map<TypeDefn, CastTransform>> fromTransformMap = new TreeMap<>();
    private EvalContext evalContext;

    public CastTransforms(EvalContext evalContext) {
        this.evalContext = evalContext;
        //
        addCaster(
            new AbstractCastTransform(NumPrimitiveTypeDefn.CARD, Executor.CHAR_SEQ_TYPE_DEF) {
                @Override
                public ValueHolder cast(ValueHolder fromHolder, TypeDefn targetTypeDefn) {
                    return castScalarToCharSeq((ScalarValue) fromHolder.getValue(), targetTypeDefn);
                }
            }
        );
        //
        addCaster(
            new AbstractCastTransform(NumPrimitiveTypeDefn.REAL, Executor.CHAR_SEQ_TYPE_DEF) {
                @Override
                public ValueHolder cast(ValueHolder fromHolder, TypeDefn targetTypeDefn) {
                    return castScalarToCharSeq((ScalarValue) fromHolder.getValue(), targetTypeDefn);
                }
            }
        );
        //
        addCaster(
            new AbstractCastTransform(NumPrimitiveTypeDefn.REAL, NumPrimitiveTypeDefn.CARD) {
                @Override
                public ValueHolder cast(ValueHolder fromHolder, TypeDefn targetType) {
                    return evalContext.getObjFactory().newDetachedHolder(
                        targetType,
                        evalContext.getObjFactory()
                                   .newCardinalValue(((Double) fromHolder.getValue().getJavaValue()).intValue())
                    );
                }
            }
        );
    }

    private ValueHolder castScalarToCharSeq(ScalarValue fromScalarValue, TypeDefn targetTypeDefn) {
        ValueHolder newHolder;
        CharacterSequence charSeqValue = evalContext.newCharSequence(fromScalarValue.asString());
        newHolder = evalContext.getObjFactory().newDetachedHolder(targetTypeDefn, charSeqValue.getOid());
        return newHolder;
    }

    private void addCaster(CastTransform castTransform) {
        Map<TypeDefn, CastTransform> fromMap = fromTransformMap.get(castTransform.getFromType());
        if (fromMap == null) {
            fromMap = new TreeMap<>();
            fromTransformMap.put(castTransform.getFromType(), fromMap);
        }
        fromMap.put(castTransform.getToType(), castTransform);
    }

    public static String castCharSequenceToJavaString(CharacterSequence characterSequence) {
        return characterSequence.toString();
    }

    public CharacterSequence stringToCharSeq(EvalContext evalContext, String jString) {
        return evalContext.newCharSequence(jString);
    }

    public ValueHolder cast(ValueHolder fromHolder, TypeDefn targetType) {
        ValueHolder targetHolder = null;
        Map<TypeDefn, CastTransform> fromCastMap = fromTransformMap.get(fromHolder.getType());
        if (fromCastMap != null) {
            CastTransform castTransform = fromCastMap.get(targetType);
            if (castTransform != null)
                targetHolder = castTransform.cast(fromHolder, targetType);
        }
        if (targetHolder == null)
            throw new SpaceX(evalContext.newRuntimeError(
                Strings.replace("can not cast from ''{0}'' to ''{1}''", fromHolder.getType(), targetType)));

        log.debug("cast from '{}' to '{}'", fromHolder, targetHolder);
        return targetHolder;
    }

    /** Cast space-to-space */
    public interface CastTransform {
        TypeDefn getFromType();
        TypeDefn getToType();
//        CastKey getFromToKey();
        ValueHolder cast(ValueHolder fromHolder, TypeDefn targetType);
    }

/*
    private class CastKey implements Comparable<CastKey> {
        TypeDefn from;
        TypeDefn to;

        public CastKey(TypeDefn from, TypeDefn to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int compareTo(CastKey key) {
            return getComparator().compare(this, key);
        }

        public TypeDefn getFrom() {
            return from;
        }

        public TypeDefn getTo() {
            return to;
        }

        public Comparator<CastKey> getComparator() {
            Comparator<CastKey> castKeyComparator = Comparator.comparing(key -> key.getFrom().getOid());
            castKeyComparator.thenComparing(key -> key.getTo().getOid());
            return castKeyComparator;
        }

        @Override
        public boolean equals(Object obj) {
            CastKey in = (CastKey) obj;
            return this.from.getOid().equals(in.from.getOid()) && this.to.getOid().equals(in.to.getOid());
        }
    }

    public CastKey makeFromToKey(TypeDefn from, TypeDefn to) {
        return new CastKey(from, to);
    }
*/

    abstract class AbstractCastTransform implements CastTransform {

        private TypeDefn fromType;
        private TypeDefn toType;

        @Override
        public TypeDefn getFromType() {
            return fromType;
        }

        @Override
        public TypeDefn getToType() {
            return toType;
        }

        public AbstractCastTransform(TypeDefn fromType, TypeDefn toType) {
            this.fromType = fromType;
            this.toType = toType;
        }

//        @Override
//        public CastKey getFromToKey() {
//            return makeFromToKey(fromType, toType);
//        }
    }

    /** Cast Space to Java for SJI */
    public interface SjiToJavaTransform {
        Object cast(Value inValue, Class targetJavaType);
    }

    /** Cast Java to Space for SJI */
    public interface SjiToSpaceTransform {
        Value cast(Object inValue, TypeDefn targetSpace);
    }

}
