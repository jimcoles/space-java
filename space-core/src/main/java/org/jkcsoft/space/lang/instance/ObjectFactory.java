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

    public VariableValueHolder newVariableValueHolder(Tuple tuple, VariableDecl decl, ScalarValue value) {
        return new VariableValueHolder(tuple, decl, value);
    }

    public DeclaredReferenceHolder<SpaceOid> newReferenceByOidHolder(Tuple parentTuple, AssociationDefn associationDecl,
                                                                     SpaceOid refToOid) {
        return newReferenceByOidHolder(parentTuple, associationDecl, newReferenceByOid(refToOid));
    }

    private DeclaredReferenceHolder<SpaceOid> newReferenceByOidHolder(Tuple parentTuple, AssociationDefn associationDecl,
                                                                      ReferenceByOid reference)
    {
        return new DeclaredReferenceHolder<>(parentTuple, associationDecl, reference);
    }

    public ReferenceByOid newReferenceByOid(SpaceOid refToOid) {
        return new ReferenceByOid(refToOid);
    }

    public TupleSet box(Tuple tuple) {
//        return new Space(newOid(), null, tuple.getDefn());
        return null;
    }

    public ValueHolder newValueHolder(Tuple tuple, Declaration decl, Value value) {
        ValueHolder holder = null;
        if (value instanceof ScalarValue)
            holder = newVariableValueHolder(tuple, ((VariableDecl) decl), ((ScalarValue) value));
        else
            holder = newReferenceByOidHolder(tuple, (AssociationDefn) decl, ((ReferenceByOid) value));
        return holder;
    }

    public ValueHolder<CardinalValue> newCardinalValueHolder(Tuple tuple, VariableDecl decl, long i) {
        return new VariableValueHolder(tuple, decl, newCardinalValue(i));
    }

    public CardinalValue newCardinalValue(long i) {
        return new CardinalValue(i);
    }

    public ValueHolder<BooleanValue> newBooleanValueHolder(Tuple tuple, VariableDecl decl, boolean aBoolean) {
        return new VariableValueHolder(tuple, decl, newBooleanValue(aBoolean));
    }

    public BooleanValue newBooleanValue(boolean aBoolean) {
        return BooleanValue.getValue(aBoolean);
    }

    public ValueHolder<CharacterValue> newCharacterValueHolder(Tuple tuple, VariableDecl decl, char character) {
        return new VariableValueHolder(tuple, decl, newCharacterValue(character));
    }

    public CharacterValue newCharacterValue(char character) {
        return new CharacterValue(character);
    }

    public ValueHolder<RealValue> newRealValueHolder(Tuple tuple, VariableDecl decl, Double aDouble) {
        return new VariableValueHolder(tuple, decl, newRealValue(aDouble));
    }

    public RealValue newRealValue(Double aDouble) {
        return new RealValue(aDouble);
    }

    public BlockContext newBlockContext(StatementBlock statementBlock, Tuple tuple) {
        return new BlockContext(statementBlock, tuple);
    }

    public TupleSetImpl newSet(SetTypeDefn defn) {
        return new TupleSetImpl(newOid(), defn);
    }

    public VoidDatum newVoidHolder() {
        return VoidDatum.VOID;
    }

    public JavaReference newJavaReference(Object targetJavaObject) {
        return new JavaReference(targetJavaObject);
    }
}
