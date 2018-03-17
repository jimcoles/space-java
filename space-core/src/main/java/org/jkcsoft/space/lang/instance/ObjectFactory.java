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

import org.jkcsoft.space.lang.ast.AssociationDefn;
import org.jkcsoft.space.lang.ast.FunctionCallExpr;
import org.jkcsoft.space.lang.ast.SpaceTypeDefn;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jim Coles
 */
public class ObjectFactory {

    private static ObjectFactory instance;

    public static ObjectFactory getInstance() {
        if (instance == null)
            instance = new ObjectFactory();
        return instance;
    }

    // instance-level

    private AtomicLong latestOid = new AtomicLong(1L);

    private ObjectFactory() {
        //no instance
    }

    public SpaceOid newOid() {
        return new SpaceOid(latestOid.incrementAndGet());
    }

    public Space newSpace(Space spcContext, SpaceTypeDefn spaceTypeDefn) {
        return new Space(newOid(), spcContext, spaceTypeDefn);
    }

    public Tuple newTuple(SpaceTypeDefn defn) {
        Tuple tuple = new Tuple(newOid(), defn);
        return tuple;
    }

    public CharacterSequence newCharacterSequence(String characters) {
        CharacterSequence characterSequence = new CharacterSequence(newOid(), characters);
        return characterSequence;
    }

    public FunctionCallContext newFunctionCall(Tuple ctxObject, FunctionCallExpr functionCallExpr, Tuple argTuple) {
        FunctionCallContext functionCallContext = new FunctionCallContext(ctxObject, functionCallExpr, argTuple);
        return functionCallContext;
    }

    public Reference newObjectReference(AssociationDefn associationDefn, SpaceOid refToOid) {
        Reference reference = new Reference(associationDefn, refToOid);
        return reference;
    }

    public Space box(Tuple tuple) {
        return new Space(newOid(), null, tuple.getDefn());
    }

    public CardinalValue newCardinalValue(int i) {
        return new CardinalValue(i);
    }

    public BooleanValue newBooleanValue(boolean aBoolean) {
        return BooleanValue.getValue(aBoolean);
    }
}
