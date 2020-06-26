/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.NSRegistry;
import org.jkcsoft.space.lang.ast.SetTypeDefn;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.*;

/**
 * Access needed by Java programs that are using Space programmatically via the builder API,
 * etc.
 */
public interface ApiExeContext {

    ObjectFactory getObjFactory();

    void trackInstanceObject(SpaceObject spaceObject);

    SpaceObject dereference(SpaceOid referenceOid) throws SpaceX;

    AstFactory getAstFactory();

    NSRegistry getNsRegistry();

    SjiService getSjiService();

    /** Writes a simple shallow set of tuples by value in tabular form. */
    String print(TupleSet tupleSet);

    TupleImpl newTupleImpl(TypeDefn defn);

    TupleSetImpl newSet(SetTypeDefn setTypeDefn);

    CharacterSequence newCharacterSequence(String stringValue);
}
