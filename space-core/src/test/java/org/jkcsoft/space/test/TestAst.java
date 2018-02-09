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
        astBuilder.initProgram("(API Builder Program)");
        //
        ObjectBuilder objBuilder = ObjectBuilder.getInstance();
        SpaceTypeDefn spaceTypeDefn = astBuilder.newSpaceTypeDefn("MyHelloSpace");
        astBuilder.getAstRoot()
            .addSpaceDefn(spaceTypeDefn)
                .addVariable(astBuilder.newVariableDefn("myIntDim", PrimitiveType.CARD))
                    .setType(PrimitiveType.CHAR);
        spaceTypeDefn
            .addVariable(astBuilder.newVariableDefn("myCharDim", PrimitiveType.CHAR))
            ;
        SpaceActionDefn mainMethod = astBuilder.newSpaceActionDefn("main");
        spaceTypeDefn.addActionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astBuilder.getAstRoot().addObjectInstance(arg1, astBuilder);

        ThisExpr thisTupleExpr = astBuilder.newThis();

        mainMethod.addAction(
            astBuilder.newActionCallExpr(
                "callPoint",
                    astBuilder.newSpacePathExpr(PathOperEnum.ASSOC_NAV, ""),
                    astBuilder.newMetaObjectRefLiteral(null),
                    astBuilder.newLiteralHolder("Hello, Space!")
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
            spex.linkAndExec(astBuilder.getAstRoot());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
