/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.instance.Index;
import org.jkcsoft.space.lang.instance.SpaceObject;
import org.jkcsoft.space.lang.instance.SpaceOid;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class wraps the base set of Space objects and enforces the
 * triggering of index modifications for each object change.
 *
 * @author Jim Coles
 */
public class ObjectTable {

    private Index objByOid = new Index();

    /**
     * The 'object' tables for 'instance' objects associated with the running program.
     * This map is added to as the program runs.
     */
    private Map<SpaceOid, SpaceObject> instObjectsIndexByOid = new TreeMap<>();

    /** Map of object by the Oid of their Type defn.  So, essentially */
    private Map<SpaceOid, SpaceObject> objectsByType = new TreeMap<>();

    /**
     * KeyValue=the referenced object's Oid.
     * Value=the set of object Oids holding reference to the key Oid.
     */
    private Map<SpaceOid, Set<SpaceOid>> objectReferenceMap = new TreeMap<>();

    public void addObject(SpaceObject sobj) {
        instObjectsIndexByOid.put(sobj.getOid(), sobj);
    }

    public SpaceObject getObjectByOid(SpaceOid oid) {
        return instObjectsIndexByOid.get(oid);
    }
}
