package org.jkcsoft.space.test;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.*;
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
        ProgSourceInfo si = new ProgSourceInfo();
        ast.newProgram(si, "");
        ObjectFactory objs = ObjectFactory.getInstance();
        //
        SpaceTypeDefn testPersonTypeDefn = ast.newSpaceTypeDefn(si, ast.newTextNode(si, "PersonType"));
//        VariableDefn firstName = ast.newVariableDefn(new ProgSourceInfo(), "firstName", PrimitiveTypeDefn.TEXT);
//        testPersonTypeDefn.addVariable(firstName);
//        VariableDefn lastName = ast.newVariableDefn(new ProgSourceInfo(), "lastName", PrimitiveTypeDefn.TEXT);
//        testPersonTypeDefn.addVariable(lastName);
//        Set space = objs.newSet(null, ast.new(null, "mySet",
//                                                     ast.newSpacePathExpr(new ProgSourceInfo(), null, "Person", null)));
        //
        Tuple testObjectTuple = objs.newTuple(testPersonTypeDefn);
//        SpaceUtils.assignOper(testObjectTuple, (NamedElement) firstName, (Assignable) textValue);
//        SpaceUtils.assignOper(testObjectTuple, (NamedElement) lastName, (Assignable) objs.newTextValue("Coles"));
//        space.addTuple(testObjectTuple);
        //
    }

    @Test
    public void testSpaceBuilder() {
        this.runTestSource("TestSpaceBuilder.space");
    }

}
