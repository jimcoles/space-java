/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.instance.SpaceObject;
import org.jkcsoft.space.lang.runtime.loaders.antlr.g2.G2AntlrParser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    private Set<ParseUnit>      parseUnits = new HashSet<>();
    private List<SpaceTypeDefn> spaceTypeDefns = new LinkedList<>();
    private List<SpaceObject>   objectHeap = new LinkedList<>();

    // ================== The starting point for using Space to execute Space programs

    // Uses Space constructs to hold a table (space) of SpaceOids to
    private ObjectFactory spaceBuilder = ObjectFactory.getInstance();
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

    SpaceProgram(String name) {
        super(name);
    }

    public SpaceTypeDefn getFirstSpaceDefn() {
        return spaceTypeDefns.get(0);
    }

    public void addObjectInstance(SpaceObject spaceObject, AstFactory astFactory) {
        objectHeap.add(spaceObject);
    }

    public List<SpaceObject> getObjectHeap() {
        return objectHeap;
    }

    // =========================================================================
    // Child adders

    public ParseUnit addParseUnit(ParseUnit parseUnit) {
        parseUnits.add(parseUnit);
        return parseUnit;
    }

    public SpaceTypeDefn addSpaceDefn(SpaceTypeDefn spaceTypeDefn) {
        spaceTypeDefns.add(spaceTypeDefn);
        // redundant
        addChild(spaceTypeDefn);
        return spaceTypeDefn;
    }

}
