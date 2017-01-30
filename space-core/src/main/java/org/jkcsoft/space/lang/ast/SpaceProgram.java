/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.instance.SpaceObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates an entire executable system as defined by Space definition elements
 * (ModelElements) and associated instances.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceProgram extends ModelElement {

    // The following "Space" types if we want to back our 'definition' model with our
    // own notions.  The problem is that it will take time to build a
    // "Space model of Space" itself.

//    private Space relationDefns;
//    private Space assocDefns;
//    private Space actionSequenceDefns;

    // TODO: indexes for fast lookup
    private List<SpaceDefn> spaceDefns = new LinkedList<>();
    private List<SpaceObject> objectHeap = new LinkedList<>();

    public SpaceProgram() {
    }

    public <T extends SpaceDefn> T addSpace(T spaceDefn) {
        spaceDefns.add(spaceDefn);
        return spaceDefn;
    }

    public SpaceDefn getFirstSpace() {
        return spaceDefns.get(0);
    }

    public void addObjectInstance(SpaceObject spaceObject, AstBuilder astBuilder) {
        objectHeap.add(spaceObject);
    }

    public List<SpaceObject> getObjectHeap() {
        return objectHeap;
    }
}
