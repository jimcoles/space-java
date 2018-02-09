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
import org.jkcsoft.space.lang.ast.SpaceActionDefn;
import org.jkcsoft.space.lang.ast.SpaceTypeDefn;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jim Coles
 */
public class ObjectBuilder {

    private static ObjectBuilder instance;

    public static ObjectBuilder getInstance() {
        if (instance == null)
            instance = new ObjectBuilder();
        return instance;
    }

    // instance-level

    private AtomicLong latestOid = new AtomicLong(1L);

    private ObjectBuilder() {
        //no instance
    }

    public SpaceOid newOid() {
        return new SpaceOid(latestOid.incrementAndGet());
    }

    public Space newSpace(Space spcContext, SpaceTypeDefn spaceTypeDefn) {
        return new Space(newOid(), spcContext, spaceTypeDefn);
    }

    public Tuple newTuple(Space space, Assignable ... values) {
        Tuple tuple = new Tuple(newOid(), space, values);
        space.addTuple(tuple);
        return tuple;
    }

    public CharacterSequence newCharacterSequence(String characters) {
        CharacterSequence characterSequence = new CharacterSequence(newOid(), characters);
        return characterSequence;
    }

    public ActionCall newAction(Space spcContext, SpaceActionDefn spcActionDefn) {
        ActionCall actionCall = new ActionCall(spcContext, spcActionDefn);
        return actionCall;
    }

    public Association newObjectReference(AssociationDefn associationDefn, SpaceOid refToOid) {
        Association association = new Association(newOid(), associationDefn, refToOid);
        return association;
    }

    public CardinalValue newCardinalValue(int i) {
        return new CardinalValue(i);
    }

    public TextValue newTextValue(String value) {
        return new TextValue(value);
    }
}
