/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * A mathematically rigorous set of operators for each type is essential to the
 * ensuring "closure" with the lofty goal of Rule "reduction". My theory is that
 * higher level notions can express operators from lower levels, e.g., vector
 * addition oper (+) defined in terms of real number addition.
 *
 * <p>Generally, the set of operators for a given type is all that is needed to
 * express {@link Rule}s while maintaining "closure", i.e., the result of the
 * operator is of the same type, e.g., int + int yields an int.
 *
 * <p>"Casting" operators build one type of object from an input of another type.
 *
 * <p>"Creation (Constructor) new" operators build new types of objects from constituents of
 * other types. These add 'quanta' to the system.
 *
 * <p>"Fusion" operations build complex things from simpler things without adding
 * 'quanta' to the system.
 *
 * <p>"Fission"
 *
 * @author Jim Coles
 */
public class Operators {

    IntegerDimension RealIntegral(TheVoidSpace val, RealDimension rdim) {
        return TheVoidSpaceImpl.SELF;
    }

//    boolean equality(RealDimension real, IntegerDimension integer) {
//        return real == O
//    }

    IntegerDimension RealDifferential(RealDimension sp1, RealDimension sp2) {
        if (sp1 == sp2)
            return TheVoidSpaceImpl.SELF.getOneI();
        else
            return TheVoidSpaceImpl.SELF.getOneI();
    }

    interface TheVoidSpace {
        default TheVoidSpace getSelf() {
            return TheVoidSpaceImpl.getInstance().getSelf();
        }

        ;
        // ID: 0
        // no form, no morphs, no definition, no meaning
    }

    //
    // ONE = diff(Any Real Dim, [SELF]), but only for that dimension
    // PI = diff(Any Real Radial, Any Real Cartesian) - the area distribution
    // E = diff()
    // Others ?
    // Rationals operators are Modulus, Division,
    //
    interface IntegerDimension {
        default IntegerDimension getOneI() {
            return TheVoidSpaceImpl.SELF;
        }

        default IntegerDimension getPi() {
            return TheVoidSpaceImpl.SELF;
        }
    }

    /**
     The "Real" category
     morphs/maps
     - create - maps @{@link TheVoidSpace} into a 1-D Real value with a specific distribution. I
     don't think it makes any sense but to assume this point distribution is "even", but all other
     references are relative anyway.
     - differential - describes the mapping of TheVoidSpace to new points. I think this has to be "1" and
     maybe be a good definition for the integer "1". I.e, every point differs from its previous point
     by "1".
     - aps
     */
    interface RealDimension {
        default RealDimension getOneR() {
            return TheVoidSpaceImpl.SELF;
        }
    }

    interface RealMorphism {

    }

    /** Represents the absence of time or space. The beginning. The Genesis. It is the only
     * Space that must exist, cannot be deleted and cannot itself morph. It has
     * all trust but it doesn't know how to do anything.
     * Recursion stops here. */
    static class TheVoidSpaceImpl implements TheVoidSpace, IntegerDimension, RealDimension {
        private static final TheVoidSpaceImpl SELF = new TheVoidSpaceImpl();

        public static TheVoidSpace getInstance() {
            return SELF;
        }

        private TheVoidSpace self = new TheVoidSpaceImpl();

        @Override
        public TheVoidSpace getSelf() {
            return self;
        }

        @Override
        public IntegerDimension getPi() {
            return null;
        }
    }

    public static class Operator {
        private String defaultName;
        private TypeDefn operType;

        public Operator(String defaultName, TypeDefn operType) {
            this.defaultName = defaultName;
            this.operType = operType;
        }

        public String getDefaultName() {
            return defaultName;
        }

        public TypeDefn getOperType() {
            return operType;
        }
    }

    public static class NavOper extends Operator {
        // "." - returns named child (primitive, simple, ref, sequence)
        public static final NavOper QUERY_CHILD = new NavOper("qcbm", null);
        // "[i] returns sequence items at index
        public static final NavOper QUERY_SEQUENCE = new NavOper("qcbidx", null);
        // "->" returns target of reference
        public static final NavOper QUERY_REFERENCE = new NavOper("qref", null);

        private NavOper(String defaultName, TypeDefn operType) {
            super(defaultName, operType);
        }

    }

    public static class BitOper extends Operator {
        public static final BitOper AND = new BitOper("bitand", null);
        public static final BitOper OR = new BitOper("bitor", null);

        private BitOper(String defaultName, TypeDefn operType) {
            super(defaultName, operType);
        }
    }

    public static class ByteOper extends Operator {
        public static final ByteOper AND = new ByteOper("byteand", null);
        public static final ByteOper OR = new ByteOper("byteor", null);

        private ByteOper(String defaultName, TypeDefn operType) {
            super(defaultName, operType);
        }
    }

    public static class BoolOper extends Operator {

        public static final BoolOper OR = new BoolOper("boolor", null);
        public static final BoolOper COND_OR = new BoolOper("boolcondor", null);
        public static final BoolOper AND = new BoolOper("booland", null);
        public static final BoolOper COND_AND = new BoolOper("boolcondor", null);
        public static final BoolOper NEGATION = new BoolOper("boolneg", null);

        private BoolOper(String defaultName, TypeDefn operType) {
            super(defaultName, operType);
        }

    }

    public static class IntCompOper extends Operator {

        public static final IntCompOper EQ = new IntCompOper("inteq", null);
        public static final IntCompOper LT = new IntCompOper("intlt", null);
        public static final IntCompOper GT = new IntCompOper("intgt", null);

        private IntCompOper(String defaultName, TypeDefn operType) {
            super(defaultName, operType);
        }
    }

    public static class IntAlgOper extends Operator {

        public static final IntAlgOper ADD = new IntAlgOper("intadd", null);
        public static final IntAlgOper SUB = new IntAlgOper("intsub", null);
        public static final IntAlgOper MULT = new IntAlgOper("intmult", null);
        public static final IntAlgOper DIV = new IntAlgOper("intdiv", null);

        private IntAlgOper(String defaultName, TypeDefn operType) {
            super(defaultName, operType);
        }
    }

}
