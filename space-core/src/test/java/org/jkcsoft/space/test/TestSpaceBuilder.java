package org.jkcsoft.space.test;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.ObjectFactory;
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
        AstFactory ast = new AstFactory();
        ast.newProgram(new CodeSourceInfo(), "");
        ObjectFactory objs = ObjectFactory.getInstance();
        //
        SpaceTypeDefn testPersonTypeDefn = ast.newSpaceTypeDefn(new CodeSourceInfo(), "PersonType");
        VariableDefn firstName = ast.newVariableDefn(new CodeSourceInfo(), "firstName", PrimitiveType.TEXT);
        testPersonTypeDefn.addVariable(firstName);
        VariableDefn lastName = ast.newVariableDefn(new CodeSourceInfo(), "lastName", PrimitiveType.TEXT);
        testPersonTypeDefn.addVariable(lastName);
        Space space = objs.newSpace(null, testPersonTypeDefn);
        //
        Tuple testObjectTuple = objs.newTuple(testPersonTypeDefn);
        TextValue textValue = objs.newTextValue("jim");
        testObjectTuple.setValue(firstName, textValue);
        testObjectTuple.setValue(lastName, objs.newTextValue("Coles"));
        space.addTuple(testObjectTuple);
        //
    }

    @Test
    public void testSpaceBuilder() {
        this.runTestSource("TestSpaceBuilder.space");
    }

}
