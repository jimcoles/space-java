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
import org.jkcsoft.space.lang.runtime.RuntimeError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class TestAst {

    @Test
    public void testBuildAndRunProgram() {
        AstFactory astFactory = new AstFactory();
        astFactory.newProgram(new CodeSourceInfo(), "(API Builder Program)");
        //
        ObjectFactory objBuilder = ObjectFactory.getInstance();
        SpaceTypeDefn spaceTypeDefn = astFactory.newSpaceTypeDefn(new CodeSourceInfo(), "MyHelloSpace");

        Schema astSchema = astFactory.newAstSchema(new CodeSourceInfo(), "TestAst");
        astSchema
            .addSpaceDefn(spaceTypeDefn)
            .getBody()
            .addVariable(astFactory.newVariableDefn(new CodeSourceInfo(), "myIntDim", PrimitiveType.CARD))
            .setType(PrimitiveType.CHAR);
        spaceTypeDefn
            .setBody(astFactory.newTypeDefnBody(new CodeSourceInfo()))
            .addVariable(astFactory.newVariableDefn(new CodeSourceInfo(), "myCharDim", PrimitiveType.CHAR))
            ;
        FunctionDefn mainMethod = astFactory.newSpaceFunctionDefn(new CodeSourceInfo(), "main");
        spaceTypeDefn.getBody().addFunctionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astFactory.getUserAstRoot().addObjectInstance(arg1, astFactory);

        ThisExpr thisTupleExpr = astFactory.newThisExpr(new CodeSourceInfo());

        mainMethod.getStatementBlock().addExpr(
            astFactory.newFunctionCallExpr(new CodeSourceInfo())
                .setFunctionRef(
                    astFactory.newSpacePathExpr(new CodeSourceInfo(), PathOperEnum.ASSOC_NAV, "", null)
                )
        );
//        astFactory.newMetaObjectRefLiteral(null),
//            astFactory.newLiteralHolder("Hello, Space!")

        //        spaceDefn.
//            .setContextSpaceDefn(new SpaceDefn() {
//                @Override
//                public VariableDefn addVariable(VariableDefn coordinateDefn) {
//                    return super.addVariable(coordinateDefn);
//                }
//            })

        Executor spex = new Executor();

        try {
            List<RuntimeError> errors = new LinkedList<>();
            spex.linkAndExec(errors, astSchema);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
