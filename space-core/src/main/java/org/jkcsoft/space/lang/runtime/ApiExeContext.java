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

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.*;

/**
 * API needed by Java programs that are using Space programmatically via the builder API,
 * etc.
 */
public interface ApiExeContext {

    AstFactory getAstFactory();

    NSRegistry getNsRegistry();

    ObjectFactory getObjFactory();

    void trackInstanceObject(SpaceObject spaceObject);

    SpaceObject dereferenceByOid(SpaceOid referenceOid) throws SpaceX;

    SjiService getSjiService();

    Space getDefaultSpace();

    Space newSpace();

    void attachTypesToUserNs(TypeDefn ... types);

    void apiAstLoadComplete();

    TupleImpl newTupleImpl(TypeDefn defn);

    CharacterSequence newCharSequence(String stringValue);

    TupleSetImpl newSet(SetTypeDefn setTypeDefn);

    SpaceObject getRef(Tuple tuple, DatumDecl datumDecl);

    /** Writes a simple shallow set of tuples by value in tabular form. */
    String print(TupleSet tupleSet);
}
