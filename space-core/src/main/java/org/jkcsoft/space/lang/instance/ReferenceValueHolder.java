/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * Holder for Reference values as opposed to primitive values (which
 * are held in {@link VariableValueHolder}s.
 *
 * @param <V> The type of the value being held. In this case must be a sub-class
 *          of {@link ReferenceValue}.
 * @param <J> The type of the raw Java value, e.g., IntAlgOper, SpaceOid.
 * @author Jim Coles
 */
public interface ReferenceValueHolder<V extends ReferenceValue<J>, J> extends ValueHolder<V, J> {

}
