/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.test;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.CharacterSequence;
import org.jkcsoft.space.lang.instance.ObjectBuilder;
import org.jkcsoft.space.lang.runtime.Executor;
import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestAst {

    @Test
    public void testBuildAndRunProgram() {
        AstBuilder astBuilder = new AstBuilder();
        astBuilder.initProgram();
        //
        ObjectBuilder objBuilder = ObjectBuilder.getInstance();

        astBuilder.initProgram();
        SpaceDefn spaceDefn = astBuilder.newSpaceDefn("MyHelloSpace");
        astBuilder.getAstRoot()
            .addSpaceDefn(spaceDefn)
                .addDimension(astBuilder.newCoordinateDefn("myIntDim", PrimitiveType.CARD))
                    .setType(PrimitiveType.CHAR);
        spaceDefn
            .addDimension(astBuilder.newCoordinateDefn("myCharDim", PrimitiveType.CHAR))
            ;
        SpaceActionDefn mainMethod = astBuilder.newSpaceActionDefn("main");
        spaceDefn.addActionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astBuilder.getAstRoot().addObjectInstance(arg1, astBuilder);
        mainMethod.addAction(
            astBuilder.newCallActionDefn(
                "JnOpSys.println",
                astBuilder.newAssignmentDefn(
                    null,
                    astBuilder.newLiteralHolder("Hello, Space!")
                )
            )
        );
//        spaceDefn.
//            .setContextSpaceDefn(new SpaceDefn() {
//                @Override
//                public CoordinateDefn addDimension(CoordinateDefn coordinateDefn) {
//                    return super.addDimension(coordinateDefn);
//                }
//            })

        Executor spex = new Executor();

        try {
            spex.exec(astBuilder.getAstRoot());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
