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

import org.jkcsoft.space.lang.instance.ScalarValue;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * A {@link TypeDefn} corresponds to the 'box' in a box-and-line type/class diagram
 * ({@link AssociationDefn} is the 'line').
 * All data have a type that is described and controlled by a TypeDefn meta object.
 * This applies to complex user types, native Space types including primitive
 * types for integers, real numbers, bytes, characters and bits, and native
 * collective types sequence, set, and stream.
 *
 * Structural elements of a TypeDefn:
 * <ul>
 *     <li>Datum declarations</li>
 *     <li>Declarations of Associations (including Projective/Extensions) to other keyed types</li>
 *     <li>Methods & Functions</li>
 *     <li>Rules</li>
 * </ul>
 *
 * <p>Kinds of Types:
 * <ul>
 *     <li>Entity - has a primary key</li>
 *     <li>View - derived from a basis Type and related.</li>
 *     <li>Simple/Scalar - primitives and types that extend primitives. Has only one unnamed value.</li>
 *     <li>Complex / Vector - a map of name/values pairs, where each value may be scalar or
 *     vector valued.</li>
 *     <li>Set of</li>
 *     <li>Sequence of</li>
 *     <li>Stream of</li>
 * </ul>
 * Structurally, a Type definition is a list of named and typed datums
 * to other Types. With Space, one Type 'extends' another via an Association.
 *
 * <p>Usage-wise, Types are the central abstraction and generalization mechanism within
 * the Space language. Types declare datum structures and may also define Functions
 * and Rules associated with a Type.
 *
 * <p>At it's most basic, a {@link TypeDefn} is just a set of constraints
 * superimposed on a byte sequence.
 *
 * Supertype of {@link ViewDefn}, which adds a selector Rule.
 *
 * @author Jim Coles
 */
public interface TypeDefn extends DatumDeclContext, Named {

    default boolean isSjiWrapper() {
        return false;
    }

    boolean isPrimitive();

    boolean isSimple();

    boolean isComplex();

    default boolean isByteKey() { return false; }

    boolean isView();

    boolean isSetType();

    boolean isSequenceType();

    boolean isStreamType();

    List<Statement> getInitializations();

    boolean hasPrimaryKey();

    KeyDefnImpl getPrimaryKeyDefn();

    Set<KeyDefnImpl> getAlternateKeyDefns();

    TypeDefn addFunctionDefn(FunctionDefn functionDefn);

    //

    boolean isAssignableTo(TypeDefn argsType);

    SequenceTypeDefn getSequenceOfType();

    SetTypeDefn getSetOfType();

    /** Returns the comparator corresponding to the primary key. Only applies for
     *  complex types - types with one or more variables. */
    Comparators.ProjectionComparator getTypeComparator();

    /** Compares two {@link org.jkcsoft.space.lang.instance.Value}s of this type.
     *  Only applies to primitive types. */
    Comparator<ScalarValue> getValueComparator();

}
