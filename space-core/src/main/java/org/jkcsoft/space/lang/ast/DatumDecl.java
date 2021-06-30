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
 * A Datum holds either a value or a reference to an object. Structurally, a Datum
 * includes the following:
 * <ul>
 *     <li><b><em>name</em></b> - The means by which the datum is accessed programmatically. optional
 *     (the name may be inherited from a basis type definition)
 *     <li><b><em>datum type</em></b> (reference) - the controlling datum type. optional -- for some datums the
 *     type is inferred.
 *     <li><b><em>value expression</em></b> - an optional expression for computing the value
 * </ul>
 *
 * <p>Value Complexity:
 * <ul>
 *       <li><b><em>scalar-valued</em></b> - A single atomic value. The meat.
 *       For primitive or simple types such as a boolean or number.</li>
 *       <li><b><em>collection-valued</b></em> - A composite value. Internally, stored by OID
 *       ref. May be of type sequence, stream, or an object of non-keyed complex type.</li>
 * </ul>
 *
 * <p>Value Directness:
 * <ul>
 *       <li><b><em>by value</b></em> - the value
 *       <li><b><em>ref by key (app key)</b></em> - Reference by key to an independent object.
 *       A by-reference slot with a key reference (and resolved id reference) to independent
 *       (keyed) objects of complex types. Key refs can be 'hard' (the link is resolved once and
 *       follows the object if the object's key values change), or 'soft' (the link is resolved
 *       by key every time it is accessed). If the target object (OID) has changed key values, the
 *       link might be broken or might have been replaced by another OID.</li>
 * </ul>
 *
 * <p>Value Dependency:
 * <ul>
 *       <li><b><em>independent</b></em> - The value is specified directly (not computed).</li>
 *       <li><b><em>dependent</b></em> - Value computed based on a controlling expression.
 *       Either by-value or by-ref depending on the expression.</li>
 * </ul>
 *
 * <p>Baseness:
 * <ul>
 *       <li><b><em>basis variable</b></em> - </li>
 *       <li><b><em>view (projection) variable</b></em> - </li>
 * </ul>
 * </p>
 * <p> E.g.:
 * <pre>
 *     type DependentType {
 *          // independent vars and refs
 *          /&#42 datum &#42/ string name;
 *          /&#42 datum &#42/ Real cost;
 *          datum Time creationTime;
 *          datum spaceTime;
 *          datum Address myAddress;  // an OID ref to an unkeyed complex object owned by this object
 *          //
 *          datum {&#064; ref}} IndependentType aRelatedThing;  // OID ref to an independent keyed object of type IndependentType
 *          //
 *          datum futureCost = {&#064; rule}} (* cost (* time ));  // a dependent variable
 *          //
 *          key (name);
 *     }
 *
 *     type IndependentType {
 *         // the assoc kind keyword: one of 'seq', 'set',
 *         // the other end of the assoc type.datum
 *         // the name of this datum
 *
 *         // this declaration links datums on each side
 *         var DependentType[.aRelatedThing] myConsumers [{&#064; kind = sequence}];
 *     }
 *
 *     // An explicit association
 *     assoc AParentChildAsc {
 *         from DependentType aRelatedThing;
 *         to IndependentType myConsumers;
 *         kind sequence;
 *     }
 * </pre>
 * </p>
 *
 * @author Jim Coles
 */
public interface DatumDecl extends Expression, Identified, Named {

    DatumDeclContext getDeclContext();

    /** The datum's type; not the structural parent (type) of the datum. */
    TypeDefn getType();

    boolean hasAssoc();

    default boolean isRef() {
        return hasAssoc();
    }

    default boolean isVariable() {
        return !hasAssoc();
    }

    default VariableDecl asVariable() {
        if (isVariable())
            return ((VariableDecl) this);
        else
            return null;
    };

    /** Compares the values of a given named variable (possibly a projection) between two tuples. */
    Comparators.DatumTupleComparator getDatumComparator();

}

