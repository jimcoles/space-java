/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
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
import org.jkcsoft.space.lang.runtime.SpaceX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The {@link Space} imple for most programmatic operations. Much of the state
 * in this impl, e.g., object tables. is managed by the controlling {@link Executor}.
 *
 * @see Space
 * @author Jim Coles
 */
public class InMemorySpaceImpl extends AbstractSpaceObject implements Space {

    private static final Logger log = LoggerFactory.getLogger(InMemorySpaceImpl.class.getSimpleName());

    /** Controlling (basis) type definitions. */
    private final Map<SpaceOid, TypeDefn> types = new TreeMap<>();

    /** Views (incl indices) that must be updated by underlying {@link VariableDecl} oid. */
    private Map<SpaceOid, ViewDefn> viewDefnsByTypeOid;

    /** All seminal state is modified by {@link Transaction}s. Persists to transaction log. */
    private final ActionQueue transactionQueue = new ActionQueue();
    /**
     * THE 'object' tables for user objects (mostly Tuples) associated with the running program.
     */
    private final Map<SpaceOid, SpaceObject> objectsByOid = new TreeMap<>();
    /**
     * References to objects by SpaceOid. key = 'to' object oid; entry='from' object
     */
    private final Map<SpaceOid, SpaceObject> references = new TreeMap<>();

    // ----------------------------- REDUNDANT STATE

    /** Redundant state that must be updated by rule firing / trigger / event. */
    private Map<SpaceOid, View> views = new TreeMap<>();

    public InMemorySpaceImpl(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
    }

    /** Tell the space what it's base types are. */
    public void allowTypes(TypeDefn ... typeDefns) {
        for (TypeDefn typeDefn : typeDefns) {
            if (typeDefn.isView())
                throw new SpaceX("{} is not a base type", typeDefn);
            types.put(typeDefn.getOid(), typeDefn);
            initKeyViews(typeDefn);
        }
    }

    private void initKeyViews(TypeDefn typeDefn) {

    }

    @Override
    public Set<TypeDefn> getComplexTypeDefs() {
        return Set.copyOf(types.values());
    }

    public void createView(ViewDefn viewDefn) {
        viewDefnsByTypeOid.put(viewDefn.getBasisType().getOid(), viewDefn);

    }

    @Override
    public List<View> getViews() {
        return List.copyOf(views.values());
    }

    @Override
    public void insert(Tuple tuple) {
        log.debug("inserting tuple {} into to space {}", tuple, this);
        validateInsert(tuple);
        objectsByOid.put(tuple.getOid(), tuple);
        updateViews(tuple);
    }

    private void validateInsert(Tuple tuple) {
        if (tuple.getDefn().isView())
            throw new SpaceX("only basis tuples may be inserted into a space", tuple);
    }

    @Override
    public void update(Tuple tuple) {
        log.debug("updating tuple {} for space {}", tuple, this);

    }

    public void trackInstanceObject(SpaceObject spaceObject) {
        // TODO set oid here
        // TODO validate type constraints including constructors and unique keys
        objectsByOid.put(spaceObject.getOid(), spaceObject);
        // TODO Trigger new object index updates by formulation transaction
//        updateViews(spaceObject);
    }

    private void updateViews(SpaceObject spaceObject) {
        if (spaceObject instanceof Tuple) {
            Tuple tuple = (Tuple) spaceObject;
            TypeDefn rootType = tuple.getDefn();
            Set<KeyDefnImpl> allKeyDefns = rootType.getAlternateKeyDefns();
            if (allKeyDefns != null)
                for (KeyDefnImpl keyDefn : allKeyDefns) {
                    Tuple keyValue = extractKeyTuple(tuple, keyDefn);

                }
        }
    }

    private Tuple extractKeyTuple(Tuple baseTuple, KeyDefn keyDefn) {
        Tuple keyTuple = getObjectFactory().newTupleImpl(keyDefn.getBasisType());
        keyDefn.getProjectionDeclList().forEach((keyVar) ->
            keyTuple.setValue(keyVar, baseTuple.get(keyVar).getValue())
        );
        return keyTuple;
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws SpaceX {
        SpaceObject spaceObject = objectsByOid.get(referenceOid);
        if (spaceObject == null)
            throw new SpaceX("Space Oid [" + referenceOid + "] not found.");
        return spaceObject;
    }

    public Tuple navToOne(Tuple fromObject, AssociationDefn assocDecl) {
        Tuple result = null;
        if (assocDecl.getAssociationKind() == AssociationKind.INDEPENDENT_TYPE) {
            if (assocDecl.getToEnd().getUpperMultiplicity() < 2) {
                Value toRef = fromObject.get(assocDecl.getFromEnd().getDatumDecl()).getValue();
                if (toRef instanceof ReferenceByOid) {
                    result = (Tuple) dereference(((ReferenceByOid) toRef).getToOid());
//                    result = getObjectFactory().newView(viewDefn):
                }
            }
            else {
                throw new SpaceX("association can have many results");
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "In Memory Space ";
    }

    @Override
    public boolean isCollective() {
        return false;
    }

    @Override
    public boolean isTuple() {
        return false;
    }

    //    private Tuple binaryKey(SpaceOid oidOne, SpaceOid oidTwo) {
//        getObjectFactory().newT
//    }
}
