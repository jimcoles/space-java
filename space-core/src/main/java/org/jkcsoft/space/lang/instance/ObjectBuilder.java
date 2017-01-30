/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.SpaceActionDefn;
import org.jkcsoft.space.lang.ast.SpaceDefn;

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

    public Space newSpace(Space spcContext, SpaceDefn spaceDefn) {
        return new Space(spcContext, spaceDefn);
    }

    public Tuple newTuple(Space space, ScalarValue ... values) {
        Tuple tuple = new Tuple(space, values);
        space.addTuple(tuple);
        return tuple;
    }

    public CharacterSequence newCharacterSequence(String characters) {
        CharacterSequence characterSequence = new CharacterSequence(newOid(), characters);
        return characterSequence;
    }

    public Action newAction(Space spcContext, SpaceActionDefn spcActionDefn) {
        Action action = new Action(spcContext, spcActionDefn);
        return action;
    }
}
