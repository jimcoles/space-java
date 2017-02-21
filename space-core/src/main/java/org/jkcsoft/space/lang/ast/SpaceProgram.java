/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.ObjectBuilder;
import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.instance.SpaceObject;
import org.jkcsoft.space.lang.runtime.loaders.antlr.g2.G2AntlrParser;

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

    private List<SpaceDefn>     spaceDefns = new LinkedList<>();
    private List<SpaceObject>   objectHeap = new LinkedList<>();

    // ================== The starting point for using Space to execute Space programs

    // Uses Space constructs to hold a table (space) of SpaceOids to
    private ObjectBuilder spaceBuilder = ObjectBuilder.getInstance();
    private G2AntlrParser spaceInSpace = new G2AntlrParser();
    {
        try {
//            spaceInSpace.load(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Space namespace = spaceBuilder.newSpace(null, null);

    // ==================

    SpaceProgram() {

    }

    public SpaceDefn addSpaceDefn(SpaceDefn spaceDefn) {
        spaceDefns.add(spaceDefn);
        // redundant
        addChild(spaceDefn);
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
