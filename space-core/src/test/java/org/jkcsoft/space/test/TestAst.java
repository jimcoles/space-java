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
import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.runtime.Executor;
import org.junit.Test;

/**
 * @author Jim Coles
 */
public class TestAst {

    @Test
    public void testBuildAndRunProgram() {
        AstFactory astFactory = new AstFactory();
        astFactory.initProgram("(API Builder Program)");
        //
        ObjectFactory objBuilder = ObjectFactory.getInstance();
        SpaceTypeDefn spaceTypeDefn = astFactory.newSpaceTypeDefn("MyHelloSpace");
        astFactory.getAstRoot()
            .addSpaceDefn(spaceTypeDefn)
                .addVariable(astFactory.newVariableDefn("myIntDim", PrimitiveType.CARD))
                    .setType(PrimitiveType.CHAR);
        spaceTypeDefn
            .addVariable(astFactory.newVariableDefn("myCharDim", PrimitiveType.CHAR))
            ;
        SpaceActionDefn mainMethod = astFactory.newSpaceActionDefn("main");
        spaceTypeDefn.addActionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astFactory.getAstRoot().addObjectInstance(arg1, astFactory);

        ThisExpr thisTupleExpr = astFactory.newThis();

        mainMethod.addAction(
            astFactory.newActionCallExpr(
                "callPoint",
                    astFactory.newSpacePathExpr(PathOperEnum.ASSOC_NAV, ""),
                    astFactory.newMetaObjectRefLiteral(null),
                    astFactory.newLiteralHolder("Hello, Space!")
            )
        );
//        spaceDefn.
//            .setContextSpaceDefn(new SpaceDefn() {
//                @Override
//                public VariableDefn addVariable(VariableDefn coordinateDefn) {
//                    return super.addVariable(coordinateDefn);
//                }
//            })

        Executor spex = new Executor();

        try {
            spex.linkAndExec(astFactory.getAstRoot());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
