/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Identified;

/**
 * {@link SpaceObject}s are either {@link Tuple}s, Sets, Sequences, or Streams.
 * In addition, primitive and {@link org.jkcsoft.space.lang.ast.SimpleType} values
 * can be boxed in an object wrapper. All objects have internal identifiers ({@link SpaceOid}s)
 * to support operations such as '=='.
 *
 * @author Jim Coles
 */
public interface SpaceObject {

    /**
     * In general, every object has constraints defining what it is and what it can relate to.
     */
    DatumType getDefn();

    /** */
    SpaceOid getOid();

    /**
     * Every object can hold some kind of value which may be a reference to another object.
     * the nature of valid objects is controlled by the {@link DatumType} of this object.
     */
//    SpaceObject addObject(ValueHolder node);

}
