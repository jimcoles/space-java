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
import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.instance.sji.SjiTuple;

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

    public TupleImpl newTupleImpl(ComplexType defn) {
        TupleImpl tuple = new TupleImpl(newOid(), defn);
        return tuple;
    }

    public SjiTuple newSjiTuple(SjiTypeDefn defn, Object jObject) {
        SjiTuple tuple = new SjiTuple(newOid(), defn, jObject);
        return tuple;
    }

    public CharacterSequence newCharacterSequence(String characters) {
        CharacterSequence characterSequence = new CharacterSequence(newOid(), characters);
        return characterSequence;
    }

    public FunctionCallContext newFunctionCall(Tuple ctxObject, FunctionCallExpr functionCallExpr, TupleImpl argTuple,
                                               ValueHolder retValHolder) {
        FunctionCallContext functionCallContext =
            new FunctionCallContext(ctxObject, functionCallExpr, argTuple, retValHolder);
        return functionCallContext;
    }

    public Reference newObjectReference(AssociationDecl associationDecl, Tuple parentTuple, SpaceOid refToOid) {
        Reference reference = new Reference(associationDecl, parentTuple, refToOid);
        return reference;
    }

    public SetSpace box(Tuple tuple) {
//        return new Space(newOid(), null, tuple.getDefn());
        return null;
    }

    public CardinalValue newCardinalValue(int i) {
        return new CardinalValue(i);
    }

    public BooleanValue newBooleanValue(boolean aBoolean) {
        return BooleanValue.getValue(aBoolean);
    }

    public CharacterValue newCharacterValue(char character) {
        return new CharacterValue(character);
    }

    public RealValue newRealValue(Double aDouble) {
        return new RealValue(aDouble);
    }

    public BlockContext newBlockContext(StatementBlock statementBlock, Tuple tuple) {
        return new BlockContext(statementBlock, tuple);
    }

    public SetSpace newSet(SetSpace contextSpace, SetTypeDefn defn) {
        return new SetSpace(newOid(), contextSpace, defn);
    }

    public VoidDatum newVoidHolder() {
        return VoidDatum.VOID;
    }
}
