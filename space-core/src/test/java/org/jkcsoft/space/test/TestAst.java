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
import org.jkcsoft.space.lang.ast.ProgSourceInfo;
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
        astFactory.newProgram(new ProgSourceInfo(), "(API Builder Program)");
        //
        ObjectFactory objBuilder = ObjectFactory.getInstance();
        SpaceTypeDefn spaceTypeDefn = astFactory.newSpaceTypeDefn(new ProgSourceInfo(), "MyHelloSpace");

        Schema astSchema = astFactory.newAstSchema(new ProgSourceInfo(), "TestAst");
        astSchema
            .addSpaceDefn(spaceTypeDefn)
            .getBody()
            .addVariable(astFactory.newVariableDefn(new ProgSourceInfo(), "myIntDim", PrimitiveTypeDefn.CARD))
            .setType(PrimitiveTypeDefn.CHAR);
        spaceTypeDefn
            .setBody(astFactory.newTypeDefnBody(new ProgSourceInfo()))
            .addVariable(astFactory.newVariableDefn(new ProgSourceInfo(), "myCharDim", PrimitiveTypeDefn.CHAR))
            ;
        FunctionDefn mainMethod = astFactory.newSpaceFunctionDefn(new ProgSourceInfo(), "main", null);
        spaceTypeDefn.getBody().addFunctionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astFactory.getUserAstRoot().addObjectInstance(arg1, astFactory);

        ThisExpr thisTupleExpr = astFactory.newThisExpr(new ProgSourceInfo());

        mainMethod.getStatementBlock().addExpr(
            astFactory.newFunctionCallExpr(new ProgSourceInfo())
                .setFunctionDefnRef(
                    astFactory.newSpacePathExpr(new ProgSourceInfo(), PathOperEnum.ASSOC_NAV, "", null)
                )
        );
//        astFactory.newMetaObjectRefLiteral(null),
//            astFactory.newPrimLiteralExpr("Hello, Space!")

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
