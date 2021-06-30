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

    public TupleImpl newTupleImpl(TypeDefn defn) {
        TupleImpl tuple = new TupleImpl(newOid(), defn);
        return tuple;
    }

    public CharacterSequence newCharacterSequence(String characters) {
        CharacterSequence characterSequence = new CharacterSequence(newOid(), characters);
        return characterSequence;
    }

    public FunctionCallContext newFunctionCall(Tuple ctxObject, FunctionCallExpr functionCallExpr, TupleImpl argTuple,
                                               ValueHolder retValHolder)
    {
        FunctionCallContext functionCallContext =
            new FunctionCallContext(ctxObject, functionCallExpr, argTuple, retValHolder);
        return functionCallContext;
    }

    public VariableValueHolder newVariableValueHolder(DatumMap parentMap, VariableDecl decl, ScalarValue value) {
        return new VariableValueHolder(parentMap, decl, value);
    }

    public DeclaredReferenceHolder<SpaceOid> newReferenceByOidHolder(DatumMap parentTuple,
                                                                     DatumDecl datumDecl,
                                                                     SpaceOid refToOid)
    {
        return newReferenceByOidHolder(parentTuple, datumDecl, newReferenceByOid(refToOid));
    }

    public DeclaredReferenceHolder<SpaceOid> newReferenceByOidHolder(DatumMap datumMap,
                                                                     DatumDecl datumDecl,
                                                                     ReferenceByOid reference)
    {
        return new DeclaredReferenceHolder(datumMap, datumDecl, reference);
    }

    public FreeReferenceHolder<SpaceOid> newFreeReferenceHolder(ObjectRefCollection fromObject, SpaceOid refToOid) {
        return newFreeReferenceHolder(fromObject, newReferenceByOid(refToOid));
    }

    public FreeReferenceHolder newFreeReferenceHolder(ObjectRefCollection fromObject, ReferenceValue referenceByOid) {
        return new FreeReferenceHolder<>(fromObject, referenceByOid);
    }

    /** Any type of value. */
    public <V extends Value<J>, J> DetachedHolder<V, J> newDetachedHolder(TypeDefn type, V value) {
        return new DetachedHolder<V, J>(type, value);
    }

    public DetachedHolder<ReferenceByOid, SpaceOid> newDetachedReferenceHolder(TypeDefn type, SpaceOid toOid) {
        return newDetachedHolder(type, newReferenceByOid(toOid));
    }

    public ReferenceByOid newReferenceByOid(SpaceOid refToOid) {
        return new ReferenceByOid(refToOid);
    }

    public ValueHolder newEmptyVarHolder(DatumMap datumMap, DatumDecl datumDecl) {
        ValueHolder holder = null;
        if (datumDecl.getType().isPrimitive()) {
            holder = newVariableValueHolder(datumMap, (VariableDecl) datumDecl, null);
        }
        else {
            holder = newReferenceByOidHolder(datumMap, datumDecl, (SpaceOid) null);
        }
        return holder;
    }

    public TupleSet box(Tuple tuple) {
//        return new Space(newOid(), null, tuple.getDefn());
        return null;
    }

    public ValueHolder newValueHolder(DatumMap datumMap, DatumDecl datumDecl, Value value) {
        ValueHolder holder = null;
        if (value instanceof ScalarValue)
            holder = newVariableValueHolder(datumMap, ((VariableDecl) datumDecl), ((ScalarValue) value));
        else
            holder = newReferenceByOidHolder(datumMap, datumDecl, ((ReferenceByOid) value));
        return holder;
    }

    public ValueHolder<CardinalValue, Long> newCardinalValueHolder(Tuple tuple, VariableDecl decl, long i) {
        return new VariableValueHolder(tuple, decl, newCardinalValue(i));
    }

    public CardinalValue newCardinalValue(long i) {
        return new CardinalValue(i);
    }

    public ValueHolder<BooleanValue, BoolEnum> newBooleanValueHolder(Tuple tuple, VariableDecl decl, boolean aBoolean) {
        return new VariableValueHolder(tuple, decl, newBooleanValue(aBoolean));
    }

    public BooleanValue newBooleanValue(boolean aBoolean) {
        return BooleanValue.getValue(aBoolean);
    }

    public ValueHolder<CharacterValue, Character> newCharacterValueHolder(Tuple tuple, VariableDecl decl,
                                                                          char character)
    {
        return new VariableValueHolder(tuple, decl, newCharacterValue(character));
    }

    public CharacterValue newCharacterValue(char character) {
        return new CharacterValue(character);
    }

    public ValueHolder<RealValue, Double> newRealValueHolder(Tuple tuple, VariableDecl decl, Double aDouble) {
        return new VariableValueHolder(tuple, decl, newRealValue(aDouble));
    }

    public RealValue newRealValue(double aDouble) {
        return new RealValue(aDouble);
    }

    public BlockContext newBlockContext(StatementBlock statementBlock, BlockDatumMap blockDatumMap) {
        return new BlockContext(statementBlock, blockDatumMap);
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

    public View newView(ViewDefn viewDefn) {
        return new View(viewDefn);
    }

    public BlockDatumMap newBlockDatumMap(DatumDeclContext datumDefn) {
        return new BlockDatumMap(datumDefn);
    }

    public ObjectRefSequenceImpl newObjectSequence(TypeDefn containedTypeDefn) {
        return new ObjectRefSequenceImpl(newOid(), containedTypeDefn);
    }
}
