/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.spm;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>The Executive that run's against a Space Program Model (SPM), which
 * is just a Space full of tuples and objects.
 *
 * <p>An Executor manages interactions between second-level elements including
 * SPM Loader's, ExprProcessor's, Querier.
 *
 * <p>The general naming pattern:
 * <ul>
 * <li>exec() methods handle executable bits like the program itself and statements.</li>
 * <li>eval() methods handle expressions</li>
 * </ul>
 * </p>
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpmExecutor extends ExprProcessor implements ExeContext, ApiExeContext {

    private static final Logger log = LoggerFactory.getLogger(SpmExecutor.class);

    @Override
    public ObjectFactory getObjFactory() {
        return null;
    }

    @Override
    public void trackInstanceObject(SpaceObject spaceObject) {

    }

    @Override
    public SpaceObject dereferenceByOid(SpaceOid referenceOid) throws SpaceX {
        return null;
    }

    @Override
    public AstFactory getAstFactory() {
        return null;
    }

    @Override
    public NSRegistry getNsRegistry() {
        return null;
    }

    @Override
    public SjiService getSjiService() {
        return null;
    }

    @Override
    public String print(TupleSet tupleSet) {
        return null;
    }

    @Override
    public TupleImpl newTupleImpl(TypeDefn defn) {
        return null;
    }

    @Override
    public TupleSetImpl newSet(SetTypeDefn setTypeDefn) {
        return null;
    }

    @Override
    public CharacterSequence newCharSequence(String stringValue) {
        return null;
    }

    @Override
    public Space getDefaultSpace() {
        return null;
    }

    @Override
    public Space newSpace() {
        return null;
    }

    @Override
    public void apiAstLoadComplete() {

    }

    @Override
    public SpaceObject getRef(Tuple tuple, DatumDecl datumDecl) {
        return null;
    }

    @Override
    public void attachTypesToUserNs(TypeDefn... types) {

    }
}
