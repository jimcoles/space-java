package org.jkcsoft.space.test;

import org.jkcsoft.space.lang.ast.AstBuilder;
import org.jkcsoft.space.lang.ast.PrimitiveType;
import org.jkcsoft.space.lang.ast.SpaceTypeDefn;
import org.jkcsoft.space.lang.instance.ObjectBuilder;
import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.instance.TextValue;
import org.jkcsoft.space.lang.instance.Tuple;
import org.junit.Test;

/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
public class TestSpaceBuilder extends TestSourceStub {

    /*
    1. Create space defn.
    2. Create space controlled by that defn.
    3. Add tuples to space.
    4. Retrieve tuples.
     */

    public void testSpaceBuilderAPI() {
        AstBuilder ast = new AstBuilder();
        ObjectBuilder objs = ObjectBuilder.getInstance();
        //
        SpaceTypeDefn testPersonTypeDefn = ast.newSpaceTypeDefn("PersonType");
        testPersonTypeDefn.addVariable(ast.newVariableDefn("firstName", PrimitiveType.TEXT));
        testPersonTypeDefn.addVariable(ast.newVariableDefn("lastName", PrimitiveType.TEXT));
        Space space = objs.newSpace(null, testPersonTypeDefn);
        //
        Tuple testObjectTuple = objs.newTuple(space);
        TextValue textValue = objs.newTextValue("jim");
        testObjectTuple.setValue("firstName", textValue);
        testObjectTuple.setValue("lastName", objs.newTextValue("Coles"));
        space.addTuple(testObjectTuple);
        //
    }

    @Test
    public void testSpaceBuilder() {
        this.runTestSource("TestSpaceBuilder.space");
    }

}
