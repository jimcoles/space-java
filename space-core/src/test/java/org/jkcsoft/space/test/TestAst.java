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
import org.jkcsoft.space.lang.instance.ObjectReference;
import org.jkcsoft.space.lang.runtime.Executor;
import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestAst {

    @Test
    public void testBuildAndRunProgram() {
        AstBuilder astBuilder = new AstBuilder();
        ObjectBuilder objBuilder = ObjectBuilder.getInstance();

        astBuilder.addRoot();
        EntityDefn spaceDefn = new EntityDefn(null, "MyHelloSpace");
        astBuilder.getAstRoot()
            .addSpace(spaceDefn)
                .addDimension(new CoordinateDefn("myIntDim", PrimitiveType.INT))
                    .setType(PrimitiveType.CHAR);
        spaceDefn
            .addDimension(new CoordinateDefn("myCharDim", PrimitiveType.CHAR))
            ;
        SpaceActionDefn mainMethod = new SpaceActionDefn(spaceDefn, "main", null);
        spaceDefn.addActionDefn(mainMethod);
        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
        astBuilder.astRoot.addObjectInstance(arg1, astBuilder);
        mainMethod.addAction(
            new CallActionDefn(spaceDefn, "JnOpSys.println",
                   new AssignmentDefn(null,
                          new ObjectReference(new CoordinateDefn(null, PrimitiveType.CHAR),  arg1.getOid())
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
