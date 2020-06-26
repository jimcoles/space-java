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

import org.jkcsoft.space.lang.ast.AssociationDefn;
import org.jkcsoft.space.lang.ast.AssociationKind;
import org.jkcsoft.space.lang.ast.KeyDefn;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.SpaceX;

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

    private final Map<SpaceOid, TypeDefn> types = new TreeMap<>();
    /** All seminal state is modified by {@link Transaction}s. Persists to transaction log. */
    private final ActionQueue transactionQueue = new ActionQueue();
    /**
     * The 'object' tables for 'instance' objects associated with the running program.
     * This map is added to as the program runs.
     */
    private final Map<SpaceOid, SpaceObject> instObjectsIndexByOid = new TreeMap<>();
    /**
     * References to objects by SpaceOid. key = 'to' object oid; entry='from' object
     */
    private final Map<SpaceOid, SpaceObject> references = new TreeMap<>();

    // ----------------------------- REDUNDANT STATE

    /** Redundantly maintained indices by user-level keys including primary key. */
    private Map<SpaceOid, Index> indexes;
    /** Redundant state that must be updated by rule firing / trigger / event. */
    private Map<SpaceOid, View> views;

    public InMemorySpaceImpl(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
    }

    @Override
    public Set<TypeDefn> getComplexTypeDefs() {
        return Set.copyOf(types.values());
    }

    @Override
    public List<View> getViews() {
        return List.copyOf(views.values());
    }

    public void trackInstanceObject(SpaceObject spaceObject) {
        // TODO set oid here
        // TODO validate type constraints including constructors and unique keys
        instObjectsIndexByOid.put(spaceObject.getOid(), spaceObject);
        // TODO Trigger new object index updates by formulation transaction
        updateViews(spaceObject);
    }

    private void updateViews(SpaceObject spaceObject) {
        if (spaceObject instanceof Tuple) {
            Tuple tuple = (Tuple) spaceObject;
            if (tuple.getDefn() instanceof TypeDefn) {
                TypeDefn rootType = (TypeDefn) tuple.getDefn();
                Set<KeyDefn> allKeyDefns = rootType.getAllKeyDefns();
                if (allKeyDefns != null)
                    for (KeyDefn keyDefn : allKeyDefns) {
                        Tuple keyValue = extractKeyTuple(tuple, keyDefn);
                    }
            }
        }
    }

    private Tuple extractKeyTuple(Tuple tuple, KeyDefn keyDefn) {
        Tuple keyTuple = getObjectFactory().newTupleImpl(keyDefn.getVars());
        keyTuple.getDefn().getVariables().forEach( (varDecl) ->
            keyTuple.setValue(varDecl, tuple.get(varDecl).getValue())
        );
        return keyTuple;
    }

    public SpaceObject dereference(SpaceOid referenceOid) throws SpaceX {
        SpaceObject spaceObject = instObjectsIndexByOid.get(referenceOid);
        if (spaceObject == null)
            throw new SpaceX("Space Oid [" + referenceOid + "] not found.");
        return spaceObject;
    }

    public Tuple navToOne(Tuple fromObject, AssociationDefn assocDecl) {
        Tuple result = null;
        if (assocDecl.getAssociationKind() == AssociationKind.INDEPENDENT) {
            if (assocDecl.getToEnd().getUpperMultiplicity() < 2) {
                Value toRef = fromObject.get(assocDecl).getValue();
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

//    private Tuple binaryKey(SpaceOid oidOne, SpaceOid oidTwo) {
//        getObjectFactory().newT
//    }
}
