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

import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.TypeDefn;

import java.util.List;
import java.util.Set;

/**
 * A Space is a collection of {@link TupleSetImpl}'s wherein objects in one collection may
 * hold a ReferenceByOid to another object. The Space is the basis for satisfying
 * a {@link org.jkcsoft.space.lang.ast.ViewDefn}. A Space has associated runtime-managed indices
 * including trees.
 *
 * Every VM has a default (main) Space.
 * A VM may hold multiple Spaces.
 * User logic may have knowledge of multiple Spaces and may move data
 * between Spaces.
 *
 * Simple programs will use the default Space.
 * More complex enterprise and service-oriented apps will contain multiple related spaces.
 *
 * Possible implementations:
 * InMemorySpaceImpl -
 * RemoteSpaceImpl - An in memory proxy to a remote state machine. This could represent
 * an external system such as a database.
 */
public interface Space {

    Set<TypeDefn> getComplexTypeDefs();

//    List<Index> getIndices();

    List<View> getViews();

    void insert(Tuple tuple);

    void update(Tuple tuple);

//    SpaceObject dereference(SpaceOid referenceOid) throws SpaceX;
//
//    void trackObject(SpaceObject spaceObject);

}
