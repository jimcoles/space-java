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
import org.jkcsoft.space.lang.instance.SpaceObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A Schema is a organizational, namespace, and security mechanism similar
 * to an XML schema.
 *
 * Encapsulates an entire executable system as defined by Space definition elements
 * (ModelElements) and associated instances.
 *
 * A Schema may contain child Schemas or {@link SpaceTypeDefn}'s or other {@link Named}
 * element types.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Schema extends NamedElement {

    private List<Schema> childSchemas = new LinkedList<>();
    private Set<ParseUnitInfo> parseUnitInfos = new HashSet<>();
    private List<SpaceTypeDefn> spaceTypeDefns = new LinkedList<>();

    // ================== The starting point for using Space to execute Space programs

    // Uses Space constructs to hold a table (space) of SpaceOids to
    private ObjectFactory spaceBuilder = ObjectFactory.getInstance();

    // ==================

    Schema(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    public SpaceTypeDefn getFirstSpaceDefn() {
        return spaceTypeDefns.get(0);
    }

    // =========================================================================
    // Child adders

    public Schema addSchema(Schema childSchema) {
        childSchemas.add(childSchema);
        //
        addChild(childSchema);
        //
        return childSchema;
    }

    public SpaceTypeDefn addSpaceDefn(SpaceTypeDefn spaceTypeDefn) {
        spaceTypeDefns.add(spaceTypeDefn);
        //
        addChild(spaceTypeDefn);
        // TODO: Add type def extends clause as references
        return spaceTypeDefn;
    }

}
