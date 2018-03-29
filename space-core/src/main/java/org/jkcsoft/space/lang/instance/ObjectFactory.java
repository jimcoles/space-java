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

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.runtime.Executor;

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

    public Tuple newTuple(TupleDefn defn) {
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
//        return new Space(newOid(), null, tuple.getDefn());
        return null;
    }

    public CardinalValue newCardinalValue(int i) {
        return new CardinalValue(i);
    }

    public BooleanValue newBooleanValue(boolean aBoolean) {
        return BooleanValue.getValue(aBoolean);
    }

    public Assignable newCharacterValue(Character character) {
        return new CharacterValue(character);
    }

    public Assignable newRealValue(Double aDouble) {
        return new RealValue(aDouble);
    }

    public BlockContext newBlockContext(StatementBlock statementBlock, Tuple tuple) {
        return new BlockContext(statementBlock, tuple);
    }

    public Assignable newHolder(DatumType datumType) {
        Assignable holder = null;
        AstFactory tmpAst = new AstFactory();
        if (datumType instanceof PrimitiveTypeDefn) {
//            holder = new Variable(null, tmpAst.newVariableDefn(), null);
            PrimitiveTypeDefn primType = (PrimitiveTypeDefn) datumType;
            if (primType == PrimitiveTypeDefn.BOOLEAN) {
                holder = newBooleanValue(false);
            }
            else if (primType == PrimitiveTypeDefn.CHAR) {
                holder = newCharacterValue(null);
            }
            else if (primType == PrimitiveTypeDefn.CARD) {
                holder = newCardinalValue(Integer.MIN_VALUE);
            }
            else if (primType == PrimitiveTypeDefn.REAL) {
                holder = newRealValue(Double.NaN);
            }
        }
        else if (datumType instanceof SpaceTypeDefn) {
            holder = newObjectReference(null, null);
        }
        else if (datumType instanceof StreamTypeDefn) {
            holder = newObjectReference(null, null);
        }
        return holder;
    }
}
